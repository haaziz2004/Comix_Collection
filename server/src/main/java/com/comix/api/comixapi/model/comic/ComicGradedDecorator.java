package com.comix.api.comixapi.model.comic;

public class ComicGradedDecorator extends BaseComicDecorator {

    public ComicGradedDecorator(IComic wrapped) {
        super(wrapped);
    }

    public double getValue() {
        return super.getValue() * 3;
    }
}
