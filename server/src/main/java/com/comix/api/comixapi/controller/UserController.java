package com.comix.api.comixapi.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.comix.api.comixapi.model.collection.Collection;
import com.comix.api.comixapi.model.comic.ComicBook;
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
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        log.info("User created with username: " + username);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}/comics/all")
    public ResponseEntity<List<ComicBook>> getAllUserComics(@PathVariable Long id) {
        log.info("Getting all comics for user with id: " + id);

        try {
            List<ComicBook> comics = userService.getAllUserComics(id);
            return ResponseEntity.ok(comics);

        } catch (IllegalArgumentException e) {
            log.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.info("Error getting all comics for user with id: " + id);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PostMapping("/{userId}/comics/add/{comicId}")
    public ResponseEntity<List<ComicBook>> addComicToUser(@PathVariable Long userId, @PathVariable Long comicId) {
        log.info("Adding comic with id: " + comicId + " to user with id: " + userId);

        try {
            List<ComicBook> comics = userService.addComicToUser(userId, comicId);
            return ResponseEntity.ok(comics);

        } catch (IllegalArgumentException e) {
            log.info(e.getMessage());
            switch (e.getMessage()) {
                case "User or comic does not exist":
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                case "Comic already exists in user's collection":
                    return ResponseEntity.status(HttpStatus.CONFLICT).build();
                default:
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } catch (Exception e) {
            log.info("Error adding comic with id: " + comicId + " to user with id: " + userId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{userId}/comics/remove/{comicId}")
    public ResponseEntity<List<ComicBook>> removeComicFromUser(@PathVariable Long userId,
            @PathVariable Long comicId) {
        log.info("Removing comic with id: " + comicId + " from user with id: " + userId);

        List<ComicBook> comics = userService.removeComicFromUser(userId, comicId);

        if (comics == null) {
            log.info("User with id: " + userId + " or comic with id: " + comicId + " does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(comics);
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody CreateUserRequestBody body) {
        String username = body.getUsername();
        String password = body.getPassword();

        log.info("Logging in user with username: " + username + " and password: " + password);

        User user = userService.login(username, password);

        if (user == null) {
            log.info("User with username: " + username + " and password: " + password + " does not exist");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        log.info("User with username: " + username + " and password: " + password + " logged in");
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        log.info("Getting user with id: " + id);

        User user = userService.getUserById(id);

        if (user == null) {
            log.info("User with id: " + id + " does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}/collection")
    public ResponseEntity<Collection> getPersonalCollection(@PathVariable Long id) {
        log.info("Getting personal collection for user with id: " + id);

        try {
            Collection collection = userService.getPersonalCollection(id);
            return ResponseEntity.ok(collection);

        } catch (IllegalArgumentException e) {
            log.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.info("Error getting personal collection for user with id: " + id);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
