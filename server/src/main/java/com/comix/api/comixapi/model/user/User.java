package com.comix.api.comixapi.model.user;

import java.util.HashSet;
import java.util.Set;

import com.comix.api.comixapi.model.collection.PersonalCollectionDatabase;
import com.comix.api.comixapi.model.usercomic.UserComic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

    @Enumerated(EnumType.STRING)
    @JsonProperty("role")
    private UserRole role = UserRole.USER;

    enum UserRole {
        USER, ADMIN
    }

    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn(name = "personal_collection_id")
    @JsonProperty("personalCollection")
    private PersonalCollectionDatabase personalCollection;

    @OneToMany(mappedBy = "user")
    private Set<UserComic> userComics = new HashSet<UserComic>();

    public User() {
    }

    public User(String name, String password) {
        this.username = name;
        this.password = password;
    }

    public User(String name, String password, PersonalCollectionDatabase personalCollection) {
        this.username = name;
        this.password = password;
        this.personalCollection = personalCollection;
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public UserRole getRole() {
        return role;
    }

    public PersonalCollectionDatabase getPersonalCollection() {
        return personalCollection;
    }

    @JsonIgnore
    public Set<UserComic> getUserComics() {
        return userComics;
    }

    public long getId() {
        return id;
    }

    // Setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public void setPersonalCollection(PersonalCollectionDatabase personalCollection) {
        this.personalCollection = personalCollection;
    }

    public void setUserComics(Set<UserComic> userComics) {
        this.userComics = userComics;
    }

    // Methods
    public void addComic(UserComic userComic) {
        userComics.add(userComic);
    }

    public void removeComic(UserComic userComic) {
        userComics.remove(userComic);
    }

    public void add(PersonalCollectionDatabase collection) {
        this.personalCollection = collection;
    }

    public void remove(PersonalCollectionDatabase collection) {
        this.personalCollection = null;
    }
}
