package com.comix.api.comixapi.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comix.api.comixapi.dao.comic.ComicDAO;
import com.comix.api.comixapi.model.comic.Comic;
import com.comix.api.comixapi.model.comic.IComic;
import com.comix.api.comixapi.model.search.SearchResults;
import com.comix.api.comixapi.model.search.SortByPublicationDate;
import com.comix.api.comixapi.requestbody.ComicSearchRequestBody;

@Service
public class ComicService {
    @Autowired
    private ComicDAO comicDAO;

    public List<Comic> getAll() {
        return comicDAO.getAll();
    }

    public Comic getById(Long id) {
        return comicDAO.getById(id);
    }

    public List<IComic> search(ComicSearchRequestBody comicSearchRequest) {
        List<IComic> comics = new ArrayList<IComic>(comicDAO.search(comicSearchRequest));

        SearchResults searchResults = new SearchResults(comics);
        if (comicSearchRequest.getSortType() == ComicSearchRequestBody.SortType.DATE) {
            searchResults.setSorter(new SortByPublicationDate());
        }
        searchResults.doSort();

        return searchResults.getSearchResults();
    }

    public boolean addComicToPersonalCollection(Long userId, Long comicId) throws Exception {
        return comicDAO.addComicToPersonalCollection(userId, comicId);
    }
}
