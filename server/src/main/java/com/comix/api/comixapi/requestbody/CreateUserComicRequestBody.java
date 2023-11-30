package com.comix.api.comixapi.requestbody;

public class CreateUserComicRequestBody {
    private Long userId;
    private Long comicId;

    public CreateUserComicRequestBody() {
    }

    public CreateUserComicRequestBody(Long userId, Long comicId) {
        this.userId = userId;
        this.comicId = comicId;
    }

    public Long getUserId() {
        return this.userId;
    }

    public Long getComicId() {
        return this.comicId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setComicId(Long comicId) {
        this.comicId = comicId;
    }
}