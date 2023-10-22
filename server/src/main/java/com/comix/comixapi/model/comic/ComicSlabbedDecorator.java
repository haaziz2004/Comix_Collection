package com.comix.comixapi.model.comic;

public class ComicSlabbedDecorator extends BaseComicDecorator {
    private final IComic wrapped;

    public ComicSlabbedDecorator(IComic wrapped) {
        super(wrapped);
        this.wrapped = wrapped;
    }

    public double getValue() {
        return wrapped.getValue() * 2;
    }
}
