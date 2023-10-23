package com.comix.api.comixapi.repository;

import org.springframework.data.repository.CrudRepository;

import com.comix.api.comixapi.model.user.User;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
}
