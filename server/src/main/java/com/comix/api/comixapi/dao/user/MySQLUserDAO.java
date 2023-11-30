package com.comix.api.comixapi.dao.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.comix.api.comixapi.exceptions.ConflictException;
import com.comix.api.comixapi.model.user.User;
import com.comix.api.comixapi.model.user.UserRowMapperNoComics;

@Repository
@Profile("mysql")
public class MySQLUserDAO implements UserDAO {

    private JdbcTemplate jdbcTemplate;

    private static final String SQL_GET_BY_ID = "select `id`, `username`, `password`, `role` from `comix_users` `users` where `users`.`id` = ? limit ?";
    private static final String SQL_LOGIN = "select `id`, `username`, `password`, `role` from `comix_users` `users` where (`users`.`username` = ? and `users`.`password` = ?) limit ?";
    private static final String SQL_REGISTER = "insert into `comix_users` (`id`, `username`, `password`, `role`) values (default, ?, ?, default)";

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public User getById(Long id) {
        try {
            return jdbcTemplate.queryForObject(SQL_GET_BY_ID, new UserRowMapperNoComics(), id, 1);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public User login(String username, String password) {
        try {
            return jdbcTemplate.queryForObject(SQL_LOGIN, new UserRowMapperNoComics(), username, password, 1);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Long register(String username, String password) throws Exception {
        User user = login(username, password);

        if (user != null) {
            throw new ConflictException("User with username " + username + " already exists");
        }

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(SQL_REGISTER, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, username);
                ps.setString(2, password);
                return ps;
            }
        }, keyHolder);

        Long userId = null;
        Number key = keyHolder.getKey();
        if (key != null) {
            userId = key.longValue();
        } else {
            throw new Exception("Unable to register user with username " + username);
        }

        return userId;
    }
}
