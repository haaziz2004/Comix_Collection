package com.comix.api.comixapi.model.user;

import java.util.HashSet;
import java.util.Set;

import com.comix.api.comixapi.model.comic.ComicBook;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty("id")
    private Long id;

    @Column(unique = true, length = 32)
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

    @OneToMany(mappedBy = "user")
    @JsonProperty("userComics")
    @JsonManagedReference
    private Set<ComicBook> userComics = new HashSet<ComicBook>();

    public User() {
    }

    public User(String name, String password) {
        this.username = name;
        this.password = password;
    }

    public User(String name, String password, Set<ComicBook> userComics) {
        this.username = name;
        this.password = password;
        this.userComics = userComics;
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

    public Set<ComicBook> getUserComics() {
        return userComics;
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

    public void setUserComics(Set<ComicBook> userComics) {
        this.userComics = userComics;
    }

    // Methods
    public void addComic(ComicBook userComic) {
        userComics.add(userComic);
    }

    public void removeComic(ComicBook userComic) {
        userComics.remove(userComic);
    }

    public Long getId() {
        return id;
    }
}
