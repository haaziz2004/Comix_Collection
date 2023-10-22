package com.comix.api.comixapi.model.comic;

public abstract class BaseComicDecorator implements IComic {
    private final IComic wrapped;

    BaseComicDecorator(IComic wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public double getValue() {
        return this.wrapped.getValue();
    }
}
