package com.comix.api.comixapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comix.api.comixapi.model.user.User;
import com.comix.api.comixapi.repository.ComicRepository;
import com.comix.api.comixapi.repository.UserRepository;
import com.comix.api.comixapi.model.comic.ComicBook;

import java.util.List;

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
            return null;
        }

        return comics;
    }

    public List<ComicBook> addComicToUser(Long userId, Long comicId) {
        User user = userRepository.findById(userId).orElse(null);
        ComicBook comic = comicRepository.findById(comicId).orElse(null);

        if (user == null || comic == null) {
            return null;
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
            return null;
        }

        user.removeComic(comic);
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
}
