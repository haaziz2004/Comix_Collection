package com.comix.api.comixapi.requestbody;

public class ComicAddComicToPersonalCollectionRequestBody {
    public Long comicId;
    public Long userId;

    public ComicAddComicToPersonalCollectionRequestBody() {
    }

    public ComicAddComicToPersonalCollectionRequestBody(Long comicId, Long userId) {
        this.comicId = comicId;
        this.userId = userId;
    }

    public Long getComicId() {
        return comicId;
    }

    public void setComicId(Long comicId) {
        this.comicId = comicId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
