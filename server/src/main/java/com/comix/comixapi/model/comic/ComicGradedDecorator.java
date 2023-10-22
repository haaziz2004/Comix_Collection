package com.comix.comixapi.model.comic;

public class ComicGradedDecorator extends BaseComicDecorator {
    private final IComic wrapped;

    public ComicGradedDecorator(IComic wrapped) {
        super(wrapped);
        this.wrapped = wrapped;
    }

    public double getValue() {
        return wrapped.getValue() * 3;
    }
}
