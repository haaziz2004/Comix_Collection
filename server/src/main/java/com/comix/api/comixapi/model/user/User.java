package com.comix.api.comixapi.model.user;

import java.util.Set;

import com.comix.api.comixapi.model.collection.PersonalCollection;
import com.comix.api.comixapi.model.usercomic.UserComic;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String email;
    private String password;

    @Enumerated(EnumType.ORDINAL)
    private UserRole role = UserRole.USER;

    enum UserRole {
        USER, ADMIN
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "personal_collection_id", referencedColumnName = "id")
    private PersonalCollection personalCollection;

    @OneToMany(mappedBy = "user")
    private Set<UserComic> userComics;

    public User() {
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public UserRole getRole() {
        return role;
    }

    public PersonalCollection getPersonalCollection() {
        return personalCollection;
    }

    public Set<UserComic> getUserComics() {
        return userComics;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public void setPersonalCollection(PersonalCollection personalCollection) {
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

    public void addComicToCollection(UserComic userComic) {
        personalCollection.addElement(userComic);
    }

    public void removeComicFromCollection(UserComic userComic) {
        personalCollection.removeElement(userComic);
    }

    public void add(PersonalCollection collection) {
        this.personalCollection = collection;
    }

    public void remove(PersonalCollection collection) {
        this.personalCollection = null;
    }
}
