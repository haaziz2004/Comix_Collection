package com.comix.api.comixapi.model.comic;

import java.sql.Timestamp;
import java.util.Set;

import com.comix.api.comixapi.model.character.ComicCharacter;
import com.comix.api.comixapi.model.collection.CollectionElement;
import com.comix.api.comixapi.model.creator.ComicCreator;

public abstract class BaseComicDecorator implements CollectionElement {
    private final CollectionElement wrapped;

    BaseComicDecorator(CollectionElement wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public double getValue() {
        return this.wrapped.getValue();
    }

    @Override
    public String getPublisher() {
        return this.wrapped.getPublisher();
    }

    @Override
    public String getSeriesTitle() {
        return this.wrapped.getSeriesTitle();
    }

    @Override
    public String getVolumeNumber() {
        return this.wrapped.getVolumeNumber();
    }

    @Override
    public String getIssueNumber() {
        return this.wrapped.getIssueNumber();
    }

    @Override
    public Timestamp getPublicationDate() {
        return this.wrapped.getPublicationDate();
    }

    @Override
    public String getDescription() {
        return this.wrapped.getDescription();
    }

    @Override
    public String getStoryTitle() {
        return this.wrapped.getStoryTitle();
    }

    @Override
    public Long getId() {
        return this.wrapped.getId();
    }

    @Override
    public boolean isSlabbed() {
        return this.wrapped.isSlabbed();
    }

    @Override
    public Long getUserId() {
        return this.wrapped.getUserId();
    }

    @Override
    public int getGrade() {
        return this.wrapped.getGrade();
    }

    @Override
    public Set<ComicCreator> getCreators() {
        return this.wrapped.getCreators();
    }

    @Override
    public Set<ComicCharacter> getPrincipleCharacters() {
        return this.wrapped.getPrincipleCharacters();
    }

    @Override
    public void addElement(CollectionElement element) {
        this.wrapped.addElement(element);
    }

    @Override
    public void removeElement(CollectionElement element) {
        this.wrapped.addElement(element);
    }

    @Override
    public Set<CollectionElement> getElements() {
        return this.wrapped.getElements();
    }

    @Override
    public int getNumberOfIssues() {
        return this.wrapped.getNumberOfIssues();
    }
}
