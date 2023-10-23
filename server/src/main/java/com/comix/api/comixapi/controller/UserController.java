package com.comix.api.comixapi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.comix.api.comixapi.model.user.User;
import com.comix.api.comixapi.requestbody.CreateUserRequestBody;
import com.comix.api.comixapi.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequestBody body) {
        String username = body.getUsername();
        String password = body.getPassword();

        log.info("Creating user with username: " + username + " and password: " + password);

        User user = userService.createUser(username, password);

        if (user == null) {
            // user already exists
            log.info("User already exists with username: " + username);
            return ResponseEntity.badRequest().build();
        }

        log.info("User created with username: " + username);
        return ResponseEntity.ok(user);
    }

}
