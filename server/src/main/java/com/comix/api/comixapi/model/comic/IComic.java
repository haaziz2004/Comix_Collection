package com.comix.api.comixapi.model.comic;

import java.util.Set;

import com.comix.api.comixapi.model.creator.Creator;
import com.comix.api.comixapi.model.user.User;
import com.comix.api.comixapi.model.character.Character;

public interface IComic {
    public double getValue();

    public String getPublisher();

    public String getSeriesTitle();

    public String getVolumeNumber();

    public String getIssueNumber();

    public String getPublicationDate();

    public Set<Creator> getCreators();

    public Set<Character> getPrincipleCharacters();

    public String getDescription();

    public String getStoryTitle();

    public Long getId();

    public Boolean getSlabbed();

    public User getUser();

    public int getGrade();
}
