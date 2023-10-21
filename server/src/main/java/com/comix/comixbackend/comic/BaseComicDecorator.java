package com.comix.comixbackend.comic;

public abstract class BaseComicDecorator implements IComic {
    private final IComic wrapped;
    private double value;

    BaseComicDecorator(IComic wrapped, double value) {
        this.wrapped = wrapped;
        this.value = value;
    }

    BaseComicDecorator(IComic wrapped) {
        this(wrapped, 5);
    }

    @Override
    public double getValue() {
        return value;
    }

    public String getPublisher() {
        return wrapped.getPublisher();
    }
}
