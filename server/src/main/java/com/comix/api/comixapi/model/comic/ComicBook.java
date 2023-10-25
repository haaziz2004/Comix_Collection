package com.comix.api.comixapi.model.comic;

import java.util.Set;

import com.comix.api.comixapi.model.character.Character;
import com.comix.api.comixapi.model.collection.CollectionElement;
import com.comix.api.comixapi.model.creator.Creator;
import com.comix.api.comixapi.model.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity(name = "comics")
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "UniquePublisherSeriesTitleVolumeNumberIssueNumberPublicationDate", columnNames = {
                "publisher", "series_title", "volume_number",
                "issue_number", "publication_date", "user_id" }) })
public class ComicBook implements IComic, CollectionElement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @Column(name = "publisher", length = 40)
    @JsonProperty("publisher")
    private String publisher;

    @Column(name = "series_title")
    @JsonProperty("seriesTitle")
    private String seriesTitle;

    @Column(name = "volume_number", length = 40)
    @JsonProperty("volumeNumber")
    private String volumeNumber;

    @Column(name = "issue_number", length = 10)
    @JsonProperty("issueNumber")
    private String issueNumber;

    @Column(name = "publication_date", length = 20)
    @JsonProperty("publicationDate")
    private String publicationDate;

    @Column(name = "story_title")
    @JsonProperty("storyTitle")
    private String storyTitle;

    @ManyToMany
    @JoinTable(name = "comic_creators", joinColumns = @JoinColumn(name = "comic_id"), inverseJoinColumns = @JoinColumn(name = "creator_id"))
    @JsonProperty("creators")
    @JsonManagedReference
    private Set<Creator> creators;

    @ManyToMany
    @JoinTable(name = "comic_characters", joinColumns = @JoinColumn(name = "comic_id"), inverseJoinColumns = @JoinColumn(name = "character_id"))
    @JsonProperty("principleCharacters")
    @JsonManagedReference
    private Set<Character> principleCharacters;

    @Column(name = "description", nullable = true)
    @JsonProperty("description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    @JsonBackReference
    private User user;

    @JsonProperty("value")
    @Column(nullable = true)
    private double value;

    @JsonProperty("grade")
    @Column(nullable = true)
    private int grade;

    @JsonProperty("slabbed")
    @Column(nullable = true)
    private Boolean slabbed;

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
            User user) {
        this(publisher, seriesTitle, volumeNumber, issueNumber, publicationDate, creators, principleCharacters,
                description, storyTitle);
        this.user = user;
    }

    public ComicBook(String publisher, String seriesTitle, String volumeNumber, String issueNumber,
            String publicationDate,
            Set<Creator> creators, Set<Character> principleCharacters, String description, String storyTitle,
            User user, double value, int graded, Boolean slabbed) {
        this(publisher, seriesTitle, volumeNumber, issueNumber, publicationDate, creators, principleCharacters,
                description, storyTitle, user);
        this.value = value;
        this.grade = graded;
        this.slabbed = slabbed;
    }

    public ComicBook(String publisher, String seriesTitle, String volumeNumber, String issueNumber,
            String publicationDate, User user, double value, int graded, Boolean slabbed) {
        this(publisher, seriesTitle, volumeNumber, issueNumber, publicationDate);
        this.user = user;
        this.value = value;
        this.grade = graded;
        this.slabbed = slabbed;
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

    public Long getId() {
        return id;
    }

    public Boolean getSlabbed() {
        return slabbed;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public void setGrade(int graded) {
        this.grade = graded;
    }

    public void setSlabbed(Boolean slabbed) {
        this.slabbed = slabbed;
    }

    // Methods
    public void addCreator(Creator creator) {
        creators.add(creator);
    }

    public void addCharacter(Character character) {
        principleCharacters.add(character);
    }

    public void removeCreator(Creator creator) {
        creators.remove(creator);
    }

    public void removeCharacter(Character character) {
        principleCharacters.remove(character);
    }

    @Override
    public String toString() {
        return "ComicBook [creators=" + creators + ", description=" + description + ", grade=" + grade
                + ", issueNumber=" + issueNumber + ", publicationDate=" + publicationDate + ", principleCharacters="
                + principleCharacters + ", publisher=" + publisher + ", seriesTitle=" + seriesTitle + ", slabbed="
                + slabbed + ", storyTitle=" + storyTitle + ", user=" + user + ", value=" + value + ", volumeNumber="
                + volumeNumber + "]";
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
}