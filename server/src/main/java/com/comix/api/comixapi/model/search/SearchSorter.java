package com.comix.api.comixapi.model.search;

import java.util.List;
import com.comix.api.comixapi.model.comic.IComic;

public interface SearchSorter {
    public void doSort(List<IComic> searchResults);
}