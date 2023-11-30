package com.comix.api.comixapi.model.comic;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import com.comix.api.comixapi.model.character.ComicCharacter;
import com.comix.api.comixapi.model.creator.ComicCreator;
import com.fasterxml.jackson.annotation.JsonManagedReference;

public class Comic implements IComic {
    private Long id;

    private String publisher;

    private String seriesTitle;

    private String volumeNumber;

    private String issueNumber;

    private Timestamp publicationDate;

    private String storyTitle;

    private String description;

    @JsonManagedReference
    private Set<ComicCreator> creators = new HashSet<ComicCreator>();

    public Comic(Long id, String publisher, String seriesTitle, String volumeNumber, String issueNumber,
            Timestamp publicationDate, String storyTitle, String description) {
        this.id = id;
        this.publisher = publisher;
        this.seriesTitle = seriesTitle;
        this.volumeNumber = volumeNumber;
        this.issueNumber = issueNumber;
        this.publicationDate = publicationDate;
        this.storyTitle = storyTitle;
        this.description = description;
    }

    public Comic(Long id, String publisher, String seriesTitle, String volumeNumber, String issueNumber,
            Timestamp publicationDate, String storyTitle, String description, Set<ComicCreator> creators) {
        this.id = id;
        this.publisher = publisher;
        this.seriesTitle = seriesTitle;
        this.volumeNumber = volumeNumber;
        this.issueNumber = issueNumber;
        this.publicationDate = publicationDate;
        this.storyTitle = storyTitle;
        this.description = description;
        this.creators = creators;
    }

    public Long getId() {
        return id;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getSeriesTitle() {
        return seriesTitle;
    }

    public String getVolumeNumber() {
        return volumeNumber;
    }

    public String getIssueNumber() {
        return issueNumber;
    }

    public Timestamp getPublicationDate() {
        return publicationDate;
    }

    public String getStoryTitle() {
        return storyTitle;
    }

    public Set<ComicCreator> getCreators() {
        return creators;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((publisher == null) ? 0 : publisher.hashCode());
        result = prime * result + ((seriesTitle == null) ? 0 : seriesTitle.hashCode());
        result = prime * result + ((volumeNumber == null) ? 0 : volumeNumber.hashCode());
        result = prime * result + ((issueNumber == null) ? 0 : issueNumber.hashCode());
        result = prime * result + ((publicationDate == null) ? 0 : publicationDate.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Comic other = (Comic) obj;
        if (publisher == null) {
            if (other.publisher != null)
                return false;
        } else if (!publisher.equals(other.publisher))
            return false;
        if (seriesTitle == null) {
            if (other.seriesTitle != null)
                return false;
        } else if (!seriesTitle.equals(other.seriesTitle))
            return false;
        if (volumeNumber == null) {
            if (other.volumeNumber != null)
                return false;
        } else if (!volumeNumber.equals(other.volumeNumber))
            return false;
        if (issueNumber == null) {
            if (other.issueNumber != null)
                return false;
        } else if (!issueNumber.equals(other.issueNumber))
            return false;
        if (publicationDate == null) {
            if (other.publicationDate != null)
                return false;
        } else if (!publicationDate.equals(other.publicationDate))
            return false;
        return true;
    }
}