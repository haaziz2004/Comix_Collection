package com.comix.api.comixapi.model.comic;

import java.util.HashSet;
import java.util.Set;

import com.comix.api.comixapi.model.character.Character;
import com.comix.api.comixapi.model.creator.Creator;
import com.comix.api.comixapi.model.usercomic.UserComic;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
                "publisher", "seriesTitle", "volume_number",
                "issue_number", "publication_date" }) })
public class ComicBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    private String publisher;
    private String seriesTitle;

    @Column(name = "volume_number", length = 40)
    private String volumeNumber;

    @Column(name = "issue_number", length = 10)
    private String issueNumber;

    @Column(name = "publication_date", length = 20)
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
    private Set<UserComic> userComics = new HashSet<UserComic>();

    protected ComicBook() {
    }

    public ComicBook(String publisher, String seriesTitle, String volumeNumber, String issueNumber,
            String publicationDate) {
        this.publisher = publisher;
        this.seriesTitle = seriesTitle;
        this.volumeNumber = volumeNumber;
        this.issueNumber = issueNumber;
        this.publicationDate = publicationDate;
    }

    public ComicBook(String publisher, String seriesTitle, String volumeNumber, String issueNumber,
            String publicationDate,
            Set<Creator> creators, Set<Character> principleCharacters, String description, String storyTitle) {
        this(publisher, seriesTitle, volumeNumber, issueNumber, publicationDate);
        this.creators = creators;
        this.principleCharacters = principleCharacters;
        this.description = description;
        this.storyTitle = storyTitle;
    }

    public ComicBook(String publisher, String seriesTitle, String volumeNumber, String issueNumber,
            String publicationDate,
            Set<Creator> creators, Set<Character> principleCharacters, String description, String storyTitle,
            Set<UserComic> userComics) {
        this(publisher, seriesTitle, volumeNumber, issueNumber, publicationDate, creators, principleCharacters,
                description, storyTitle);
        this.userComics = userComics;
    }

    // Getters
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

    public void setVolumeNumber(String volumeNumber) {
        this.volumeNumber = volumeNumber;
    }

    public void setIssueNumber(String issueNumber) {
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

    @JsonIgnore
    public Set<UserComic> getUserComics() {
        return userComics;
    }

    public void setUserComics(Set<UserComic> userComics) {
        this.userComics = userComics;
    }
}