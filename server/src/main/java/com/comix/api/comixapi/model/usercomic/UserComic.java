package com.comix.api.comixapi.model.usercomic;

import com.comix.api.comixapi.model.collection.CollectionElement;
import com.comix.api.comixapi.model.comic.ComicBook;
import com.comix.api.comixapi.model.comic.IComic;
import com.comix.api.comixapi.model.user.User;

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
public class UserComic extends CollectionElement implements IComic {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private double value;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "comic_id", nullable = false)
    private ComicBook comic;

    public UserComic() {
    }

    public UserComic(User user, ComicBook comic, double value) {
        this.user = user;
        this.comic = comic;
        this.value = value;
    }

    public UserComic(User user, ComicBook comic) {
        this(user, comic, 0);
    }

    public ComicBook getComic() {
        return comic;
    }

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
}
