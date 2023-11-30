package com.comix.api.comixapi.model.comic;

import com.comix.api.comixapi.model.collection.CollectionElement;

public class ComicSlabbedDecorator extends BaseComicDecorator {

    public ComicSlabbedDecorator(CollectionElement wrapped) {
        super(wrapped);
    }

    public double getValue() {
        return super.getValue() * 2;
    }
}
