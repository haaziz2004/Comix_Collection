package com.comix.api.comixapi.model.comic;

import java.sql.Timestamp;
import java.util.Set;

import com.comix.api.comixapi.model.creator.ComicCreator;

public interface IComic {
    public Long getId();

    public String getPublisher();

    public String getSeriesTitle();

    public String getVolumeNumber();

    public String getIssueNumber();

    public Timestamp getPublicationDate();

    public String getDescription();

    public String getStoryTitle();

    public Set<ComicCreator> getCreators();
}
