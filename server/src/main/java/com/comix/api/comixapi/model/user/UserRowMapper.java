package com.comix.api.comixapi.model.user;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

import com.comix.api.comixapi.model.comic.UserComic;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserRowMapper implements RowMapper<User> {

    @Override
    @Nullable
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        Long id = resultSet.getLong("id");
        String username = resultSet.getString("username");
        String password = resultSet.getString("password");
        String role = resultSet.getString("role");

        String jsonString = resultSet.getString("user_comics");

        if (jsonString == null) {
            return new User(id, username, password, role, new HashSet<UserComic>());
        }

        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<List<List<Object>>> typeReference = new TypeReference<List<List<Object>>>() {
        };

        try {
            List<List<Object>> dataArray = objectMapper.readValue(jsonString, typeReference);
            // Process the array elements
            for (List<Object> innerList : dataArray) {
                System.out.println(innerList);
            }
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

}
