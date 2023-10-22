package com.comix.api.comixapi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.comix.api.comixapi.model.user.User;
import com.comix.api.comixapi.repository.UserRepository;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(PersonalCollectionController.class);

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/create")
    public User createUser() {
        User user = new User("Username", "Test@gmail.com", "password");

        return userRepository.save(user);
    }

}
