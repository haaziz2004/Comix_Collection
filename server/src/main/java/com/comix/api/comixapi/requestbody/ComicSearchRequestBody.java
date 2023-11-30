package com.comix.api.comixapi.requestbody;

public class ComicSearchRequestBody {
    private String queryString;
    private SearchType searchType;
    private SortType sortType;

    public enum SearchType {
        EXACT, PARTIAL
    }

    public enum SortType {
        ALPHABETICAL, DATE
    }

    public ComicSearchRequestBody() {
    }

    public ComicSearchRequestBody(String queryString, SearchType searchType, SortType sortType) {
        this.queryString = queryString;
        this.searchType = searchType;
        this.sortType = sortType;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public SearchType getSearchType() {
        return searchType;
    }

    public void setSearchType(SearchType searchType) {
        this.searchType = searchType;
    }

    public SortType getSortType() {
        return sortType;
    }

    public void setSortType(SortType sortType) {
        this.sortType = sortType;
    }

}
