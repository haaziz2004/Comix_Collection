package com.comix.api.comixapi.model.comic;

import com.comix.api.comixapi.model.collection.CollectionElement;

public class ComicGradedDecorator extends BaseComicDecorator {
    public ComicGradedDecorator(CollectionElement wrapped) {
        super(wrapped);
    }

    public double getValue() {
        int grade = super.getGrade();
        if (grade == 1) {
            return super.getValue() * 0.1;
        }
        return Math.log10(grade) * super.getValue();
    }
}
