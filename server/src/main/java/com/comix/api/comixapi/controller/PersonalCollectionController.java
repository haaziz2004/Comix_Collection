package com.comix.api.comixapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.comix.api.comixapi.model.collection.PersonalCollection;
import com.comix.api.comixapi.model.user.User;
import com.comix.api.comixapi.repository.UserRepository;
import com.comix.api.comixapi.service.PersonalCollectionService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/collection")
public class PersonalCollectionController {
    private static final Logger log = LoggerFactory.getLogger(PersonalCollectionController.class);

    private final PersonalCollectionService personalCollectionService;
    private final UserRepository userRepository;

    public PersonalCollectionController(PersonalCollectionService personalCollectionService,
            UserRepository userRepository) {
        this.personalCollectionService = personalCollectionService;
        this.userRepository = userRepository;
    }

    // @PostMapping("/create")
    // public ResponseEntity<String> createPersonalCollection(@RequestParam Long
    // userId) {
    // User user = userRepository.findById(userId).orElse(null);
    // if (user == null) {
    // return ResponseEntity.notFound().build();
    // }

    // PersonalCollection collection =
    // personalCollectionService.createPersonalCollection(user);

    // return ResponseEntity.ok("Personal collection created and associated with the
    // user.");
    // }

    @PostMapping("/create")
    public ResponseEntity<String> createPersonalCollection() {
        User user = userRepository.findById((long) 1).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        PersonalCollection collection = personalCollectionService.createPersonalCollection(user);

        return ResponseEntity.ok("Personal collection created and associated with the user.");
    }

    @GetMapping("/get")
    public ResponseEntity<PersonalCollection> getPersonalCollection(@RequestParam Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        PersonalCollection collection = user.getPersonalCollection();

        return ResponseEntity.ok(collection);
    }

    // @PostMapping("/addComic")
    // public ResponseEntity<String> addComicToCollection(@RequestParam Long userId,
    // @RequestParam Long comicId) {
    // User user = userRepository.findById(userId).orElse(null);
    // if (user == null) {
    // return ResponseEntity.notFound().build();
    // }

    // PersonalCollection collection = user.getPersonalCollection();
    // if (collection == null) {
    // return ResponseEntity.notFound().build();
    // }

    // personalCollectionService.addComicToCollection(collection, comicId);

    // return ResponseEntity.ok("Comic added to collection.");
    // }

}
