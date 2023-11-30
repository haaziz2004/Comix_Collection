package com.comix.api.comixapi.dao.usercomic;

import java.util.List;

import com.comix.api.comixapi.model.comic.UserComic;

public interface UserComicDAO {
    UserComic getById(Long id);

    List<UserComic> getAll(Long userId);
}
