package com.comix.api.comixapi.repository;

import org.springframework.data.repository.CrudRepository;

import com.comix.api.comixapi.model.usercomic.UserComic;

public interface UserComicRepository extends CrudRepository<UserComic, Long> {

}
