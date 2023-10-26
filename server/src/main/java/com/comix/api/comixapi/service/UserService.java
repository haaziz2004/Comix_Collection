package com.comix.api.comixapi.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comix.api.comixapi.model.collection.Collection;
import com.comix.api.comixapi.model.collection.CollectionElement;
import com.comix.api.comixapi.model.comic.ComicBook;
import com.comix.api.comixapi.model.comic.ComicGradedDecorator;
import com.comix.api.comixapi.model.comic.ComicSlabbedDecorator;
import com.comix.api.comixapi.model.comic.IComic;
import com.comix.api.comixapi.model.user.User;
import com.comix.api.comixapi.repository.ComicRepository;
import com.comix.api.comixapi.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ComicRepository comicRepository;

    public User createUser(String username, String password) {
        // check if user already exists
        if (userRepository.findByUsername(username) != null) {
            return null;
        }

        User user = new User(username, password);

        // Save the User entity to the database
        userRepository.save(user);

        return user;
    }

    public List<ComicBook> getAllUserComics(Long userId) {
        List<ComicBook> comics = comicRepository.findAllByUserId(userId);

        if (comics == null) {
            throw new IllegalArgumentException("User does not exist");
        }

        return comics;
    }

    public List<ComicBook> addComicToUser(Long userId, Long comicId) {
        User user = userRepository.findById(userId).orElse(null);
        ComicBook comic = comicRepository.findById(comicId).orElse(null);

        if (user == null || comic == null) {
            throw new IllegalArgumentException("User or comic does not exist");
        }

        // check if comic is already in user's collection
        // only check "publisher", "series_title", "volume_number", "issue_number",
        // "publication_date"
        Set<ComicBook> comics = user.getUserComics();
        for (ComicBook c : comics) {
            if (c.getPublisher().equals(comic.getPublisher()) && c.getSeriesTitle().equals(comic.getSeriesTitle())
                    && c.getVolumeNumber().equals(comic.getVolumeNumber())
                    && c.getIssueNumber().equals(comic.getIssueNumber())
                    && c.getPublicationDate().equals(comic.getPublicationDate())) {
                // comic already exists in user's collection
                throw new IllegalArgumentException("Comic already exists in user's collection");
            }
        }

        // create a new comic with identical fields except also initialize the user,
        // slabbed, grade, and value fields
        ComicBook userComic = new ComicBook(comic.getPublisher(), comic.getSeriesTitle(), comic.getVolumeNumber(),
                comic.getIssueNumber(), comic.getPublicationDate(), user, 0, 0, false);

        // add to comic repository
        comicRepository.save(userComic);
        user.addComic(userComic);
        userRepository.save(user);

        return user.getUserComics().stream().toList();
    }

    public List<ComicBook> removeComicFromUser(Long userId, Long comicId) {
        User user = userRepository.findById(userId).orElse(null);
        ComicBook comic = comicRepository.findById(comicId).orElse(null);

        if (user == null || comic == null) {
            throw new IllegalArgumentException("User or comic does not exist");
        }

        user.removeComic(comic);
        comicRepository.delete(comic);

        // save repository
        userRepository.save(user);

        return user.getUserComics().stream().toList();
    }

    public User login(String username, String password) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            // user does not exist
            return null;
        }

        if (!user.getPassword().equals(password)) {
            // incorrect password
            return null;
        }

        return user;
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public Collection getPersonalCollection(Long userId) {
        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            throw new IllegalArgumentException("User does not exist");
        }

        Set<ComicBook> comics = user.getUserComics();

        Set<IComic> decoratedComics = new HashSet<>();

        for (IComic comic : comics) {
            IComic decoratedComic = comic; // Start with the original comic

            if (comic.getSlabbed()) {
                decoratedComic = new ComicSlabbedDecorator(decoratedComic);
            }

            if (comic.getGrade() > 0) {
                decoratedComic = new ComicGradedDecorator(decoratedComic);
            }

            decoratedComics.add(decoratedComic);
        }

        Collection collection = new Collection();

        for (IComic comic : comics) {
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
