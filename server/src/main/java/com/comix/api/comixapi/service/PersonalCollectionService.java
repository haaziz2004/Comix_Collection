package com.comix.api.comixapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comix.api.comixapi.model.collection.PersonalCollection;
import com.comix.api.comixapi.model.user.User;
import com.comix.api.comixapi.repository.ComicRepository;
import com.comix.api.comixapi.repository.PersonalCollectionRepository;
import com.comix.api.comixapi.repository.UserRepository;

@Service
public class PersonalCollectionService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PersonalCollectionRepository collectionRepository;
    @Autowired
    private ComicRepository comicRepository;

    public PersonalCollection createPersonalCollection(User user) {
        PersonalCollection collection = new PersonalCollection();
        collection.setUser(user);

        user.add(collection);

        userRepository.save(user);

        return collectionRepository.save(collection);
    }

    // public PersonalCollection addComicToCollection(PersonalCollection collection,
    // Long comicId) {
    // collection.addElement(comicRepository.findById(comicId).orElse(null));

    // collection.addElement();

    // return collectionRepository.save(collection);
    // }
}
