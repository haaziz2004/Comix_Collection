package com.comix.api.comixapi.model.user;

import java.util.HashSet;
import java.util.Set;

import com.comix.api.comixapi.model.comic.UserComic;
import com.fasterxml.jackson.annotation.JsonManagedReference;

public class User {
    private Long id;

    private String username;

    private String password;

    private UserRole role = UserRole.USER;

    enum UserRole {
        USER, ADMIN
    }

    @JsonManagedReference
    private Set<UserComic> userComics = new HashSet<UserComic>();

    public User() {
    }

    public User(String name, String password) {
        this.username = name;
        this.password = password;
    }

    public User(Long id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = UserRole.valueOf(role);
    }

    public User(Long id, String username, String password, String role, Set<UserComic> userComics) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = UserRole.valueOf(role);
        this.userComics = userComics;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public Set<UserComic> getUserComics() {
        return userComics;
    }

    public void setUserComics(Set<UserComic> userComics) {
        this.userComics = userComics;
    }

    public void addComic(UserComic userComic) {
        userComics.add(userComic);
    }

    public void removeComic(UserComic userComic) {
        userComics.remove(userComic);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((username == null) ? 0 : username.hashCode());
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
        User other = (User) obj;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        return true;
    }

}
