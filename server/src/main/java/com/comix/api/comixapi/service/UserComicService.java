package com.comix.api.comixapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.comix.api.comixapi.model.collection.PersonalCollectionDatabase;
import com.comix.api.comixapi.controller.PersonalCollectionController;
import com.comix.api.comixapi.model.collection.PersonalCollection;
import com.comix.api.comixapi.model.comic.ComicBook;
import com.comix.api.comixapi.model.user.User;
import com.comix.api.comixapi.model.usercomic.UserComic;
import com.comix.api.comixapi.repository.ComicRepository;
import com.comix.api.comixapi.repository.PersonalCollectionRepository;
import com.comix.api.comixapi.repository.UserComicRepository;
import com.comix.api.comixapi.repository.UserRepository;

@Service
public class UserComicService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PersonalCollectionRepository collectionRepository;
    @Autowired
    private ComicRepository comicRepository;
    @Autowired
    private UserComicRepository userComicRepository;

    private static final Logger log = LoggerFactory.getLogger(UserComicService.class);

    public UserComic createUserComic(Long userId, Long comicId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            log.info("User with id: " + userId + " does not exist");
            return null;
        }

        ComicBook comic = comicRepository.findById(comicId).orElse(null);
        if (comic == null) {
            log.info("Comic with id: " + comicId + " does not exist");
            return null;
        }

        UserComic userComic = new UserComic(user, comic);
        log.info("Created user comic: " + userComic.toString());
        userComic.setUser(user);

        PersonalCollectionDatabase userCollection = user.getPersonalCollection();

        userCollection.addUserComic(userComic);
        user.addComic(userComic);

        userRepository.save(user);
        collectionRepository.save(userCollection);

        return userComicRepository.save(userComic);
    }

}
