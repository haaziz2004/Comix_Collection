package com.comix.api.comixapi.dao.comic;

import java.util.List;

import com.comix.api.comixapi.model.comic.Comic;
import com.comix.api.comixapi.requestbody.ComicSearchRequestBody;

public interface ComicDAO {
    List<Comic> getAll();

    Comic getById(Long id);

    List<Comic> search(ComicSearchRequestBody requestBody);

    boolean addComicToPersonalCollection(Long userId, Long comicId) throws Exception;
}
