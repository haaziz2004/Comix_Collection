package com.comix.api.comixapi.model.comic;

public class ComicSlabbedDecorator extends BaseComicDecorator {

    public ComicSlabbedDecorator(IComic wrapped) {
        super(wrapped);
    }

    public double getValue() {
        return super.getValue() * 2;
    }
}
