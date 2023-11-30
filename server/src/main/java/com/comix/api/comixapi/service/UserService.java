package com.comix.api.comixapi.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comix.api.comixapi.dao.user.UserDAO;
import com.comix.api.comixapi.dao.usercomic.UserComicDAO;
import com.comix.api.comixapi.exceptions.ConflictException;
import com.comix.api.comixapi.exceptions.UserNotFoundException;
import com.comix.api.comixapi.model.collection.Collection;
import com.comix.api.comixapi.model.collection.CollectionElement;
import com.comix.api.comixapi.model.comic.ComicGradedDecorator;
import com.comix.api.comixapi.model.comic.ComicSlabbedDecorator;
import com.comix.api.comixapi.model.comic.UserComic;
import com.comix.api.comixapi.model.user.User;

@Service
public class UserService {
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private UserComicDAO userComicDAO;

    public User getById(Long id) {
        User user = userDAO.getById(id);

        if (user == null) {
            throw new UserNotFoundException(id);
        }

        return user;
    }

    public User login(String username, String password) {
        User user = userDAO.login(username, password);

        if (user == null) {
            throw new UserNotFoundException(username);
        }

        return user;
    }

    public Long register(String username, String password) throws Exception {
        try {
            return userDAO.register(username, password);
        } catch (ConflictException e) {
            throw new ConflictException("Username already exists");
        } catch (Exception e) {
            throw e;
        }
    }

    public Collection getPersonalCollection(Long userId) {
        User user = userDAO.getById(userId);

        if (user == null) {
            throw new UserNotFoundException(userId);
        }

        Set<UserComic> userComics = new HashSet<UserComic>(userComicDAO.getAll(userId));

        if (userComics.isEmpty()) {
            return new Collection();
        }

        for (CollectionElement comic : userComics) {
            CollectionElement decoratedComic = comic; // Start with the original comic

            if (comic.isSlabbed()) {
                decoratedComic = new ComicSlabbedDecorator(decoratedComic);
            }

            if (comic.getGrade() > 0) {
                decoratedComic = new ComicGradedDecorator(decoratedComic);
            }
        }

        Collection collection = new Collection();

        for (CollectionElement comic : userComics) {
            // Find or create the Publisher level
            Collection publisherLevel = findOrCreatePublisherLevel(collection, comic.getPublisher());

            // Find or create the SeriesTitle level within the Publisher level
            Collection seriesLevel = findOrCreateSeriesLevel(publisherLevel, comic.getSeriesTitle());

            // Find or create the VolumeNumber level within the SeriesTitle level
            Collection volumeLevel = findOrCreateVolumeLevel(seriesLevel, comic.getVolumeNumber());

            // Find or create the IssueNumber level within the VolumeNumber level
            Collection issueLevel = findOrCreateIssueLevel(volumeLevel, comic.getIssueNumber());

            // Add the comic to the IssueNumber level
            issueLevel.addElement(comic);
        }

        return collection;

    }

    // Helper functions to find or create levels
    private Collection findOrCreatePublisherLevel(Collection collection, String publisher) {
        for (CollectionElement publisherLevel : collection.getElements()) {
            if (publisherLevel.getPublisher().equals(publisher)) {
                return (Collection) publisherLevel;
            }
        }
        Collection newPublisher = new Collection();
        collection.addElement(newPublisher);
        return newPublisher;
    }

    // Implement similar helper functions for SeriesTitle, VolumeNumber, and
    // IssueNumber levels
    private Collection findOrCreateSeriesLevel(Collection collection, String seriesTitle) {
        for (CollectionElement seriesLevel : collection.getElements()) {
            if (seriesLevel.getSeriesTitle().equals(seriesTitle)) {
                return (Collection) seriesLevel;
            }
        }
        Collection newSeries = new Collection();
        collection.addElement(newSeries);
        return newSeries;
    }

    private Collection findOrCreateVolumeLevel(Collection collection, String volumeNumber) {
        for (CollectionElement volumeLevel : collection.getElements()) {
            if (volumeLevel.getVolumeNumber().equals(volumeNumber)) {
                return (Collection) volumeLevel;
            }
        }
        Collection newVolume = new Collection();
        collection.addElement(newVolume);
        return newVolume;
    }

    private Collection findOrCreateIssueLevel(Collection collection, String issueNumber) {
        for (CollectionElement issueLevel : collection.getElements()) {
            if (issueLevel.getIssueNumber().equals(issueNumber)) {
                return (Collection) issueLevel;
            }
        }
        Collection newIssue = new Collection();
        collection.addElement(newIssue);
        return newIssue;
    }

}
