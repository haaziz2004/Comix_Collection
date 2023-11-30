package com.comix.api.comixapi.model.creator;

import java.util.HashSet;
import java.util.Set;

import com.comix.api.comixapi.model.comic.UserComic;
import com.fasterxml.jackson.annotation.JsonBackReference;

public class UserComicCreator {
    private Long id;

    private String name;

    @JsonBackReference
    private Set<UserComic> userComics = new HashSet<UserComic>();

    public UserComicCreator() {
    }

    public UserComicCreator(String name) {
        this.name = name;
    }

    public UserComicCreator(String name, Set<UserComic> userComics) {
        this.name = name;
        this.userComics = userComics;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<UserComic> getUserComics() {
        return userComics;
    }

    public void setUserComics(Set<UserComic> userComics) {
        this.userComics = userComics;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        UserComicCreator other = (UserComicCreator) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

}
