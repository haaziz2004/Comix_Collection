package com.comix.api.comixapi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.comix.api.comixapi.exceptions.ConflictException;
import com.comix.api.comixapi.exceptions.UserNotFoundException;
import com.comix.api.comixapi.model.collection.Collection;
import com.comix.api.comixapi.model.user.User;
import com.comix.api.comixapi.requestbody.UserLoginRequestBody;
import com.comix.api.comixapi.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @RequestMapping("/getById/{id}")
    public ResponseEntity<User> getById(@PathVariable String id) {
        log.info("Getting user with id: {}", id);

        try {
            User user = userService.getById(Long.parseLong(id));
            return ResponseEntity.ok(user);
        } catch (UserNotFoundException e) {
            log.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.info(e.getMessage());
            log.info("Error getting user with id: {}", id);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody UserLoginRequestBody userLoginRequestBody) {
        log.info("Logging in user with username: {}", userLoginRequestBody.getUsername());

        try {
            User user = userService.login(userLoginRequestBody.getUsername(), userLoginRequestBody.getPassword());
            return ResponseEntity.ok(user);
        } catch (UserNotFoundException e) {
            log.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            log.info(e.getMessage());
            log.info("Error logging in user with username: {}", userLoginRequestBody.getUsername());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Long> register(@RequestBody UserLoginRequestBody userLoginRequestBody) {
        log.info("Registering user with username: {}", userLoginRequestBody.getUsername());
        try {
            Long registeredUserId = userService.register(userLoginRequestBody.getUsername(),
                    userLoginRequestBody.getPassword());
            return ResponseEntity.ok(registeredUserId);
        } catch (ConflictException e) {
            log.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e) {
            log.info(e.getMessage());
            log.info("Error registering user with username: {}", userLoginRequestBody.getUsername());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}/collection")
    public ResponseEntity<Collection> getPersonalCollection(@PathVariable Long id) {
        log.info("Getting personal collection for user with id: " + id);

        try {
            Collection collection = userService.getPersonalCollection(id);

            if (collection.getElements().isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(collection);

        } catch (UserNotFoundException e) {
            log.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.info(e.getMessage());
            log.info("Error getting personal collection for user with id: " + id);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
