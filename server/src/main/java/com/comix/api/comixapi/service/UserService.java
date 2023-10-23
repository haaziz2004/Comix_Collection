package com.comix.api.comixapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comix.api.comixapi.model.collection.PersonalCollectionDatabase;
import com.comix.api.comixapi.model.user.User;
import com.comix.api.comixapi.repository.ComicRepository;
import com.comix.api.comixapi.repository.PersonalCollectionRepository;
import com.comix.api.comixapi.repository.UserComicRepository;
import com.comix.api.comixapi.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PersonalCollectionRepository collectionRepository;
    @Autowired
    private ComicRepository comicRepository;
    @Autowired
    private UserComicRepository userComicRepository;

    public User createUser(String username, String password) {
        // check if user already exists
        if (userRepository.findByUsername(username) != null) {
            return null;
        }

        User user = new User(username, password);
        PersonalCollectionDatabase collectionDatabase = new PersonalCollectionDatabase();

        // Set the User entity's personalCollection to the CollectionDatabase entity
        user.setPersonalCollection(collectionDatabase);

        collectionDatabase.setUser(user);

        // Save the User entity to the database
        userRepository.save(user);

        return user;
    }
}
