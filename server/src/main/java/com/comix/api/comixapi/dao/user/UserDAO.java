package com.comix.api.comixapi.dao.user;

import com.comix.api.comixapi.model.user.User;

public interface UserDAO {
    User getById(Long id);

    User login(String username, String password);

    Long register(String username, String password) throws Exception;
}
