package com.comix.api.comixapi.model.creator;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

import com.comix.api.comixapi.model.comic.ComicBook;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity(name = "creators")
public class Creator {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String name;

    @ManyToMany(mappedBy = "creators")
    @JsonBackReference
    private Set<ComicBook> comics = new HashSet<ComicBook>();

    protected Creator() {
    }

    public Creator(String name, Set<ComicBook> comics) {
        this.name = name;
        this.comics = comics;
    }

    public Creator(String name) {
        this.name = name;
    }

    public Set<ComicBook> getComics() {
        return comics;
    }

    public void setComics(Set<ComicBook> comics) {
        this.comics = comics;
    }

    // Getters
    public String getName() {
        return name;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }
}
