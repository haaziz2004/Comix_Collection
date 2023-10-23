package com.comix.api.comixapi.model.usercomic;

import com.comix.api.comixapi.model.collection.PersonalCollectionDatabase;
import com.comix.api.comixapi.model.collection.CollectionElement;
import com.comix.api.comixapi.model.comic.ComicBook;
import com.comix.api.comixapi.model.comic.IComic;
import com.comix.api.comixapi.model.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "user_comics", uniqueConstraints = { @UniqueConstraint(columnNames = { "user_id", "comic_id" }) })
public class UserComic implements IComic, CollectionElement {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "comic_id", nullable = false)
    @JsonProperty("comic")
    private ComicBook comic;

    @ManyToOne
    @JoinColumn(name = "personalCollection_id", nullable = false)
    private PersonalCollectionDatabase personalCollection;

    @JsonProperty("value")
    private double value = 0;

    @JsonProperty("graded")
    private int graded = 0;

    @JsonProperty("slabbed")
    private boolean slabbed = false;

    public UserComic() {
    }

    public UserComic(User user, ComicBook comic, double value, int graded, boolean slabbed,
            PersonalCollectionDatabase personalCollection) {
        this.user = user;
        this.comic = comic;
        this.value = value;
        this.graded = graded;
        this.slabbed = slabbed;
    }

    public UserComic(User user, ComicBook comic, double value) {
        this.user = user;
        this.comic = comic;
        this.value = value;
    }

    public UserComic(User user, ComicBook comic, PersonalCollectionDatabase personalCollection) {
        this.user = user;
        this.comic = comic;
        this.personalCollection = personalCollection;
    }

    public UserComic(User user, ComicBook comic) {
        this.user = user;
        this.comic = comic;
    }

    public ComicBook getComic() {
        return comic;
    }

    @JsonIgnore
    public User getUser() {
        return user;
    }

    @Override
    public double getValue() {
        return value;
    }

    @Override
    public int getNumberOfIssues() {
        return 1;
    }

    public void setComic(ComicBook comic) {
        this.comic = comic;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @JsonIgnore
    public PersonalCollectionDatabase getPersonalCollection() {
        return personalCollection;
    }

    public void setPersonalCollection(PersonalCollectionDatabase personalCollection) {
        this.personalCollection = personalCollection;
    }

    public int getGraded() {
        return graded;
    }

    public void setGraded(int graded) {
        this.graded = graded;
    }

    public boolean getSlabbed() {
        return slabbed;
    }

    public void setSlabbed(boolean slabbed) {
        this.slabbed = slabbed;
    }

}
