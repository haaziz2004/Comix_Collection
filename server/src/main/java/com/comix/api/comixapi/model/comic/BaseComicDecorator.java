package com.comix.api.comixapi.model.comic;

import java.util.Set;

import com.comix.api.comixapi.model.creator.Creator;
import com.comix.api.comixapi.model.user.User;
import com.comix.api.comixapi.model.character.Character;
import com.comix.api.comixapi.model.collection.CollectionElement;

public abstract class BaseComicDecorator implements IComic {
    private final IComic wrapped;

    BaseComicDecorator(IComic wrapped) {
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
    public String getPublicationDate() {
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
    public Boolean getSlabbed() {
        return this.wrapped.getSlabbed();
    }

    @Override
    public User getUser() {
        return this.wrapped.getUser();
    }

    @Override
    public int getGrade() {
        return this.wrapped.getGrade();
    }

    @Override
    public Set<Creator> getCreators() {
        return this.wrapped.getCreators();
    }

    @Override
    public Set<Character> getPrincipleCharacters() {
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
