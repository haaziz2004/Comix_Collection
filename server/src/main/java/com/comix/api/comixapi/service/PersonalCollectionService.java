package com.comix.api.comixapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.comix.api.comixapi.model.collection.PersonalCollectionDatabase;
import com.comix.api.comixapi.model.comic.ComicBook;
import com.comix.api.comixapi.controller.PersonalCollectionController;
import com.comix.api.comixapi.model.collection.PersonalCollection;
import com.comix.api.comixapi.model.user.User;
import com.comix.api.comixapi.model.usercomic.UserComic;
import com.comix.api.comixapi.repository.ComicRepository;
import com.comix.api.comixapi.repository.PersonalCollectionRepository;
import com.comix.api.comixapi.repository.UserComicRepository;
import com.comix.api.comixapi.repository.UserRepository;

@Service
public class PersonalCollectionService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PersonalCollectionRepository collectionRepository;
    @Autowired
    private UserComicRepository userComicRepository;
    @Autowired
    private ComicRepository comicRepository;

    public PersonalCollectionDatabase getPersonalCollection(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return null;
        }

        PersonalCollectionDatabase collection = user.getPersonalCollection();

        return collection;
    }

    public PersonalCollectionDatabase addComicToCollection(Long userId, Long comicId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return null;
        }

        PersonalCollectionDatabase collection = user.getPersonalCollection();
        if (collection == null) {
            return null;
        }

        ComicBook comic = comicRepository.findById(comicId).orElse(null);
        if (comic == null) {
            return null;
        }

        UserComic userComic = new UserComic(user, comic, collection);
        userComicRepository.save(userComic);

        collection.addUserComic(userComic);

        return collectionRepository.save(collection);
    }
}
