package com.comix.api.comixapi.model.user;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

import com.comix.api.comixapi.model.comic.UserComic;

public class UserRowMapperNoComics implements RowMapper<User> {

    @Override
    @Nullable
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        Long id = resultSet.getLong("id");
        String username = resultSet.getString("username");
        String password = resultSet.getString("password");
        String role = resultSet.getString("role");

        return new User(id, username, password, role, new HashSet<UserComic>());
    }

}
