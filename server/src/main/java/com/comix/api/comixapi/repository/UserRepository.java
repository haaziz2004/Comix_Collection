package com.comix.api.comixapi.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.comix.api.comixapi.model.comic.ComicBook;
import com.comix.api.comixapi.model.user.User;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
}
