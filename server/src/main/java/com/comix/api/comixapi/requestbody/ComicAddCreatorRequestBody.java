package com.comix.api.comixapi.requestbody;

public class ComicAddCreatorRequestBody {
    public String creatorName;

    public ComicAddCreatorRequestBody() {
    }

    public ComicAddCreatorRequestBody(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String name) {
        this.creatorName = name;
    }
}
