package com.comix.api.comixapi.model.character;

import java.util.HashSet;
import java.util.Set;

import com.comix.api.comixapi.model.comic.Comic;
import com.fasterxml.jackson.annotation.JsonBackReference;

public class ComicCharacter {
    private Long id;

    private String name;

    @JsonBackReference
    private Set<Comic> comics = new HashSet<Comic>();

    public ComicCharacter() {
    }

    public ComicCharacter(String name) {
        this.name = name;
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

    public Set<Comic> getComics() {
        return comics;
    }

    public void setComics(Set<Comic> comics) {
        this.comics = comics;
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
        ComicCharacter other = (ComicCharacter) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }
}
