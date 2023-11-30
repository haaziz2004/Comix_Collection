package com.comix.api.comixapi.dao.usercomic;

import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.comix.api.comixapi.model.comic.UserComic;
import com.comix.api.comixapi.model.comic.UserComicRowMapper;

@Repository
@Profile("mysql")
public class MySQLUserComicDAO implements UserComicDAO {

    private JdbcTemplate jdbcTemplate;

    private static final String SQL_GET_ALL_BY_USER_ID = "SELECT `id`, `publisher`, `series_title`, `volume_number`, `issue_number`, `publication_date`, `story_title`, `description`, `value`, `grade`, `slabbed`, `user_id`, COALESCE((SELECT JSON_ARRAYAGG(`name`) FROM `comix_user_comics_to_user_comic_creators` JOIN `comix_user_comic_creators` ON `comix_user_comics_to_user_comic_creators`.`user_comic_creator_id` = `comix_user_comic_creators`.`id` WHERE `comix_user_comics_to_user_comic_creators`.`user_comic_id` = `comix_user_comics`.`id`), JSON_ARRAY()) AS `creators`, COALESCE((SELECT JSON_ARRAYAGG(`name`) FROM `comix_user_comics_to_user_comic_characters` JOIN `comix_user_comic_characters` ON `comix_user_comics_to_user_comic_characters`.`user_comic_character_id` = `comix_user_comic_characters`.`id` WHERE `comix_user_comics_to_user_comic_characters`.`user_comic_id` = `comix_user_comics`.`id`), JSON_ARRAY()) AS `characters` FROM `comix_user_comics` WHERE `user_id` = ?";

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public UserComic getById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getById'");
    }

    @Override
    public List<UserComic> getAll(Long userId) {
        List<UserComic> userComics = jdbcTemplate.query(SQL_GET_ALL_BY_USER_ID, new UserComicRowMapper(), userId);
        return userComics != null ? userComics : Collections.emptyList();
    }

}
