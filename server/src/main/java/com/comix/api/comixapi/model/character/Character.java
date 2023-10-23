package com.comix.api.comixapi.model.character;

import java.util.HashSet;
import java.util.Set;

import com.comix.api.comixapi.model.comic.ComicBook;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

@Entity(name = "characters")
public class Character {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String name;

    @ManyToMany(mappedBy = "principleCharacters")
    @JsonBackReference
    private Set<ComicBook> comics = new HashSet<ComicBook>();

    protected Character() {
    }

    public Character(String name, Set<ComicBook> comics) {
        this.name = name;
        this.comics = comics;
    }

    public Character(String name) {
        this.name = name;
    }

    // Getters
    public String getName() {
        return name;
    }

    public Set<ComicBook> getComics() {
        return comics;
    }

    public void setComics(Set<ComicBook> comics) {
        this.comics = comics;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }
}
