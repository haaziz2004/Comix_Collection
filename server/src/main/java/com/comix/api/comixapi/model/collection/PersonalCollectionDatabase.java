package com.comix.api.comixapi.model.collection;

import java.util.HashSet;
import java.util.Set;

import com.comix.api.comixapi.model.user.User;
import com.comix.api.comixapi.model.usercomic.UserComic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "personal_collections")
public class PersonalCollectionDatabase {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "personalCollection")
    @JsonProperty("userComics")
    private Set<UserComic> userComics = new HashSet<UserComic>();

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public PersonalCollectionDatabase() {
    }

    public PersonalCollectionDatabase(Set<UserComic> userComics, User user) {
        this.userComics = userComics;
        this.user = user;
    }

    public PersonalCollectionDatabase(User user) {
        this.user = user;
    }

    public Set<UserComic> getUserComics() {
        return userComics;
    }

    public void setUserComics(Set<UserComic> userComics) {
        this.userComics = userComics;
    }

    public void addUserComic(UserComic userComic) {
        this.userComics.add(userComic);
    }

    public void removeUserComic(UserComic userComic) {
        this.userComics.remove(userComic);
    }

    @JsonIgnore
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}