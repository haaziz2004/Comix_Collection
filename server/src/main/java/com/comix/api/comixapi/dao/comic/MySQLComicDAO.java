package com.comix.api.comixapi.dao.comic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.comix.api.comixapi.exceptions.ComicNotFoundException;
import com.comix.api.comixapi.exceptions.ConflictException;
import com.comix.api.comixapi.exceptions.UserNotFoundException;
import com.comix.api.comixapi.model.comic.Comic;
import com.comix.api.comixapi.model.comic.ComicRowMapper;
import com.comix.api.comixapi.model.comic.ComicRowMapperNoCreators;
import com.comix.api.comixapi.model.creator.ComicCreator;
import com.comix.api.comixapi.model.user.User;
import com.comix.api.comixapi.model.user.UserRowMapper;
import com.comix.api.comixapi.requestbody.ComicSearchRequestBody;
import com.comix.api.comixapi.requestbody.ComicSearchRequestBody.SearchType;

@Repository
@Profile("mysql")
public class MySQLComicDAO implements ComicDAO {

    private JdbcTemplate jdbcTemplate;

    private static final String SQL_GET_ALL = "select `id`, `publisher`, `series_title`, `volume_number`, `issue_number`, `publication_date`, `story_title`, `description` from `comix_comics` limit ?";
    private static final String SQL_GET_BY_ID = "SELECT `id`, `publisher`, `series_title`, `volume_number`, `issue_number`, `publication_date`, `story_title`, `description`, COALESCE( ( SELECT JSON_ARRAYAGG( ( SELECT `name` FROM `comix_comic_creators` `comics_comicsToComicCreators_comicCreator` WHERE `comics_comicsToComicCreators_comicCreator`.`id` = `comics_comicsToComicCreators`.`comic_creator_id` LIMIT ? ) ) FROM `comix_comics_to_comic_creators` `comics_comicsToComicCreators` WHERE `comics_comicsToComicCreators`.`comic_id` = `comics`.`id` ), JSON_ARRAY() ) AS `comic_creators` FROM `comix_comics` `comics` WHERE `comics`.`id` = ? LIMIT ?";
    private static final String SQL_SEARCH_PARTIAL = "select `id`, `publisher`, `series_title`, `volume_number`, `issue_number`, `publication_date`, `story_title`, `description` from `comix_comics` where `comix_comics`.`series_title` like ?";
    private static final String SQL_SEARCH_EXACT = "select `id`, `publisher`, `series_title`, `volume_number`, `issue_number`, `publication_date`, `story_title`, `description` from `comix_comics` where `comix_comics`.`series_title` = ?";
    private static final String SQL_GET_USER = "select `id`, `username`, `password`, `role`, coalesce((select json_arrayagg(json_array(`id`, `publisher`, `series_title`, `volume_number`, `issue_number`, `publication_date`, `story_title`, `description`, `value`, `grade`, `slabbed`, `user_id`)) from `comix_user_comics` `users_userComics` where (`users_userComics`.`user_id` = `users`.`id` and (`users_userComics`.`user_id` = ? and `users_userComics`.`publisher` = ? and `users_userComics`.`issue_number` = ? and `users_userComics`.`publication_date` = ? and `users_userComics`.`series_title` = ? and `users_userComics`.`volume_number` = ?))), json_array()) as `user_comics` from `comix_users` `users` where `users`.`id` = ? limit ?";
    private static final String SQL_INSERT_USER_COMIC = "insert into `comix_user_comics` (`id`, `publisher`, `series_title`, `volume_number`, `issue_number`, `publication_date`, `story_title`, `description`, `value`, `grade`, `slabbed`, `user_id`) values (default, ?, ?, ?, ?, ?, ?, ?, default, default, default, ?)";
    private static final String SQL_INSERT_USER_COMIC_CREATORS = "insert into `comix_user_comic_creators` (`id`, `name`) values (default, ?)";
    private static final String SQL_INSERT_USER_COMICS_TO_USER_COMIC_CREATORS = "insert into `comix_user_comics_to_user_comic_creators` (`user_comic_id`, `user_comic_creator_id`) values (?, ?)";

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Comic> getAll() {
        return jdbcTemplate.query(SQL_GET_ALL, new ComicRowMapperNoCreators(), 100);
    }

    @Override
    public Comic getById(Long id) {
        try {
            return jdbcTemplate.queryForObject(SQL_GET_BY_ID, new ComicRowMapper(), 1, id, 1);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Comic> search(ComicSearchRequestBody requestBody) {
        String queryString = requestBody.getQueryString();
        SearchType searchType = requestBody.getSearchType();

        if (searchType == SearchType.EXACT) {
            return jdbcTemplate.query(SQL_SEARCH_EXACT, new ComicRowMapperNoCreators(), queryString);
        } else if (searchType == SearchType.PARTIAL) {
            return jdbcTemplate.query(SQL_SEARCH_PARTIAL, new ComicRowMapperNoCreators(), "%" + queryString + "%");
        }

        return null;
    }

    @Override
    public boolean addComicToPersonalCollection(Long userId, Long comicId) throws Exception {
        Comic comic = this.getById(comicId);
        if (comic == null) {
            throw new ComicNotFoundException(comicId);
        }
        User user = jdbcTemplate.queryForObject(SQL_GET_USER, new UserRowMapper(), userId, comic.getPublisher(),
                comic.getIssueNumber(), comic.getPublicationDate(), comic.getSeriesTitle(), comic.getVolumeNumber(),
                userId, 1);

        if (user == null) {
            throw new UserNotFoundException(userId);
        }

        if (!user.getUserComics().isEmpty()) {
            throw new ConflictException(
                    "User with id " + userId + " already has comic with id " + comicId + " in their collection.");
        }

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(
                        SQL_INSERT_USER_COMIC,
                        Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, comic.getPublisher());
                ps.setString(2, comic.getSeriesTitle());
                ps.setString(3, comic.getVolumeNumber());
                ps.setString(4, comic.getIssueNumber());
                ps.setTimestamp(5, comic.getPublicationDate());
                ps.setString(6, comic.getStoryTitle());
                ps.setString(7, comic.getDescription());
                ps.setLong(8, userId);

                return ps;
            }
        }, keyHolder);

        // Retrieve the generated key
        Long userComicId = null;
        Number key = keyHolder.getKey();
        if (key != null) {
            userComicId = key.longValue();
        } else {
            throw new Exception("Failed to add comic with id " + userComicId + " to user with id " + userId + ".");
        }

        Set<Long> creatorIds = new HashSet<Long>();

        for (ComicCreator creator : comic.getCreators()) {
            jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement ps = connection.prepareStatement(
                            SQL_INSERT_USER_COMIC_CREATORS,
                            Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, creator.getName());

                    return ps;
                }
            }, keyHolder);

            // Retrieve the generated key
            Long creatorId = null;
            key = keyHolder.getKey();
            if (key != null) {
                creatorId = key.longValue();
            } else {
                throw new Exception("Failed to add comic creator with name " + creator.getName() + ".");
            }

            creatorIds.add(creatorId);
        }

        for (Long creatordId : creatorIds) {
            jdbcTemplate.update(SQL_INSERT_USER_COMICS_TO_USER_COMIC_CREATORS, userComicId, creatordId);
        }

        return true;
    }
}
