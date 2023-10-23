package com.comix.api.comixapi.model.comic;

public class ComicGradedDecorator extends BaseComicDecorator {
    private int grade;

    public ComicGradedDecorator(IComic wrapped, int grade) {
        super(wrapped);
        this.grade = grade;
    }

    public double getValue() {
        return super.getValue() * grade;
    }
}
