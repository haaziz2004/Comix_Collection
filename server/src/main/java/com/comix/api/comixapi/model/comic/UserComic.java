package com.comix.api.comixapi.model.comic;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import com.comix.api.comixapi.model.character.ComicCharacter;
import com.comix.api.comixapi.model.collection.CollectionElement;
import com.comix.api.comixapi.model.creator.ComicCreator;
import com.fasterxml.jackson.annotation.JsonManagedReference;

public class UserComic implements CollectionElement, IComic {

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

    @JsonManagedReference
    private Set<ComicCharacter> principleCharacters = new HashSet<ComicCharacter>();

    private double value = 0.0;

    private int grade = 0;

    private boolean slabbed = false;

    private Long userId;

    public UserComic() {
    }

    public UserComic(Long id, String publisher, String seriesTitle, String volumeNumber, String issueNumber,
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

    public UserComic(Long id, String publisher, String seriesTitle, String volumeNumber, String issueNumber,
            Timestamp publicationDate, String storyTitle, String description, double value, int grade,
            boolean slabbed) {
        this.id = id;
        this.publisher = publisher;
        this.seriesTitle = seriesTitle;
        this.volumeNumber = volumeNumber;
        this.issueNumber = issueNumber;
        this.publicationDate = publicationDate;
        this.storyTitle = storyTitle;
        this.description = description;
        this.value = value;
        this.grade = grade;
        this.slabbed = slabbed;
    }

    public UserComic(Long id, String publisher, String seriesTitle, String volumeNumber, String issueNumber,
            Timestamp publicationDate, String storyTitle, String description, Set<ComicCreator> creators,
            Set<ComicCharacter> principleCharacters, double value, int grade, boolean slabbed, Long userId) {
        this.id = id;
        this.publisher = publisher;
        this.seriesTitle = seriesTitle;
        this.volumeNumber = volumeNumber;
        this.issueNumber = issueNumber;
        this.publicationDate = publicationDate;
        this.storyTitle = storyTitle;
        this.description = description;
        this.creators = creators;
        this.principleCharacters = principleCharacters;
        this.value = value;
        this.grade = grade;
        this.slabbed = slabbed;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getSeriesTitle() {
        return seriesTitle;
    }

    public void setSeriesTitle(String seriesTitle) {
        this.seriesTitle = seriesTitle;
    }

    public String getVolumeNumber() {
        return volumeNumber;
    }

    public void setVolumeNumber(String volumeNumber) {
        this.volumeNumber = volumeNumber;
    }

    public String getIssueNumber() {
        return issueNumber;
    }

    public void setIssueNumber(String issueNumber) {
        this.issueNumber = issueNumber;
    }

    public Timestamp getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Timestamp publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getStoryTitle() {
        return storyTitle;
    }

    public void setStoryTitle(String storyTitle) {
        this.storyTitle = storyTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<ComicCreator> getCreators() {
        return creators;
    }

    public void setCreators(Set<ComicCreator> creators) {
        this.creators = creators;
    }

    public Set<ComicCharacter> getPrincipleCharacters() {
        return principleCharacters;
    }

    public void setPrincipleCharacters(Set<ComicCharacter> principleCharacters) {
        this.principleCharacters = principleCharacters;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public boolean isSlabbed() {
        return slabbed;
    }

    public void setSlabbed(boolean slabbed) {
        this.slabbed = slabbed;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void addCreator(ComicCreator creator) {
        creators.add(creator);
    }

    public void addCharacter(ComicCharacter character) {
        principleCharacters.add(character);
    }

    public void removeCreator(ComicCreator creator) {
        creators.remove(creator);
    }

    public void removeCharacter(ComicCharacter character) {
        principleCharacters.remove(character);
    }

    @Override
    public int getNumberOfIssues() {
        return 1;
    }

    @Override
    public void addElement(CollectionElement element) {
    }

    @Override
    public void removeElement(CollectionElement element) {
    }

    @Override
    public Set<CollectionElement> getElements() {
        return null;
    }

    @Override
    public String toString() {
        return "UserComic [id=" + id + ", publisher=" + publisher + ", seriesTitle=" + seriesTitle + ", volumeNumber="
                + volumeNumber + ", issueNumber=" + issueNumber + ", publicationDate=" + publicationDate
                + ", storyTitle=" + storyTitle + ", description=" + description + ", creators=" + creators
                + ", principleCharacters=" + principleCharacters + ", value=" + value + ", grade=" + grade
                + ", slabbed=" + slabbed + ", userId=" + userId + "]";
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
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
        UserComic other = (UserComic) obj;
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
        if (userId == null) {
            if (other.userId != null)
                return false;
        } else if (!userId.equals(other.userId))
            return false;
        return true;
    }
}