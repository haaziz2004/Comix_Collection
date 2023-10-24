package com.comix.api.comixapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comix.api.comixapi.model.user.User;
import com.comix.api.comixapi.repository.ComicRepository;
import com.comix.api.comixapi.repository.UserRepository;
import com.comix.api.comixapi.model.collection.PersonalCollection;
import com.comix.api.comixapi.model.comic.ComicBook;

import java.util.List;
import java.util.Set;

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

    public PersonalCollection getPersonalCollection(Long userId) {
        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            throw new IllegalArgumentException("User does not exist");
        }

        Set<ComicBook> comics = user.getUserComics();

        PersonalCollection collection = new PersonalCollection();
        // create 3 collection for 3 different publishers
        PersonalCollection publisher1 = new PersonalCollection();
        PersonalCollection publisher2 = new PersonalCollection();
        PersonalCollection publisher3 = new PersonalCollection();

        collection.addElement(publisher1);
        collection.addElement(publisher2);
        collection.addElement(publisher3);

        // add comics to random publisher
        for (ComicBook comic : comics) {
            int random = (int) (Math.random() * 3);
            if (random == 0) {
                publisher1.addElement(comic);
            } else if (random == 1) {
                publisher2.addElement(comic);
            } else {
                publisher3.addElement(comic);
            }
        }

        return collection;
    }
}
