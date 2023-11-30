package com.comix.api.comixapi.model.collection;

import java.sql.Timestamp;
import java.util.Set;

import com.comix.api.comixapi.model.creator.ComicCreator;
import com.comix.api.comixapi.model.character.ComicCharacter;

public interface CollectionElement {
    Long getId();

    String getPublisher();

    String getSeriesTitle();

    String getVolumeNumber();

    String getIssueNumber();

    Timestamp getPublicationDate();

    String getStoryTitle();

    String getDescription();

    Set<ComicCreator> getCreators();

    Set<ComicCharacter> getPrincipleCharacters();

    double getValue();

    int getGrade();

    boolean isSlabbed();

    Long getUserId();

    int getNumberOfIssues();

    void addElement(CollectionElement element);

    void removeElement(CollectionElement element);

    Set<CollectionElement> getElements();
}
