package com.comix.api.comixapi.model.comic;

import java.util.Set;

import com.comix.api.comixapi.model.character.Character;
import com.comix.api.comixapi.model.creator.Creator;
import com.comix.api.comixapi.model.usercomic.UserComic;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.JoinColumn;

@Entity(name = "comics")
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "UniquePublisherSeriesTitleVolumeNumberIssueNumberPublicationDate", columnNames = {
                "publisher", "seriesTitle", "volumeNumber",
                "issueNumber", "publicationDate" }) })
public class ComicBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    private String publisher;
    private String seriesTitle;
    private int volumeNumber;
    private int issueNumber;
    private String publicationDate;
    private String storyTitle;

    @ManyToMany
    @JoinTable(name = "comic_creators", joinColumns = @JoinColumn(name = "comic_id"), inverseJoinColumns = @JoinColumn(name = "creator_id"))
    private Set<Creator> creators;

    @ManyToMany
    @JoinTable(name = "comic_characters", joinColumns = @JoinColumn(name = "comic_id"), inverseJoinColumns = @JoinColumn(name = "character_id"))
    private Set<Character> principleCharacters;

    @Column(nullable = true)
    private String description;

    @OneToMany(mappedBy = "comic")
    private Set<UserComic> userComics;

    protected ComicBook() {
    }

    public ComicBook(String publisher, String seriesTitle, int volumeNumber, int issueNumber, String publicationDate) {
        this.publisher = publisher;
        this.seriesTitle = seriesTitle;
        this.volumeNumber = volumeNumber;
        this.issueNumber = issueNumber;
        this.publicationDate = publicationDate;
    }

    public ComicBook(String publisher, String seriesTitle, int volumeNumber, int issueNumber, String publicationDate,
            Set<Creator> creators, Set<Character> principleCharacters, String description, String storyTitle) {
        this(publisher, seriesTitle, volumeNumber, issueNumber, publicationDate);
        this.creators = creators;
        this.principleCharacters = principleCharacters;
        this.description = description;
        this.storyTitle = storyTitle;
    }

    // Getters
    public String getPublisher() {
        return publisher;
    }

    public String getSeriesTitle() {
        return seriesTitle;
    }

    public int getVolumeNumber() {
        return volumeNumber;
    }

    public int getIssueNumber() {
        return issueNumber;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public Set<Creator> getCreators() {
        return creators;
    }

    public Set<Character> getPrincipleCharacters() {
        return principleCharacters;
    }

    public String getDescription() {
        return description;
    }

    public String getStoryTitle() {
        return storyTitle;
    }

    // Setters
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setSeriesTitle(String seriesTitle) {
        this.seriesTitle = seriesTitle;
    }

    public void setVolumeNumber(int volumeNumber) {
        this.volumeNumber = volumeNumber;
    }

    public void setIssueNumber(int issueNumber) {
        this.issueNumber = issueNumber;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public void setCreators(Set<Creator> creators) {
        this.creators = creators;
    }

    public void setPrincipleCharacters(Set<Character> principleCharacters) {
        this.principleCharacters = principleCharacters;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStoryTitle(String storyTitle) {
        this.storyTitle = storyTitle;
    }

    @Override
    public String toString() {
        return String.format(
                "ComicBook[id=%d, publisher='%s', seriesTitle='%s', volumeNumber='%d', issueNumber='%d', publicationDate='%s', creators='%s', principleCharacters='%s', description='%s']",
                id, publisher, seriesTitle, volumeNumber, issueNumber, publicationDate, creators, principleCharacters,
                description);
    }
}