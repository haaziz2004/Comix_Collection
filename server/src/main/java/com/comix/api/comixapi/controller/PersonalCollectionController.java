package com.comix.api.comixapi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.comix.api.comixapi.model.collection.PersonalCollectionDatabase;
import com.comix.api.comixapi.requestbody.CreateUserComicRequestBody;
import com.comix.api.comixapi.service.PersonalCollectionService;

@RestController
@RequestMapping("/collection")
public class PersonalCollectionController {
    private static final Logger log = LoggerFactory.getLogger(PersonalCollectionController.class);

    private final PersonalCollectionService personalCollectionService;

    public PersonalCollectionController(PersonalCollectionService personalCollectionService) {
        this.personalCollectionService = personalCollectionService;
    }

    @GetMapping("/get/{userId}")
    public ResponseEntity<PersonalCollectionDatabase> getPersonalCollection(@PathVariable Long userId) {
        log.info("Getting personal collection for user with id: " + userId);
        PersonalCollectionDatabase collection = personalCollectionService.getPersonalCollection(userId);

        if (collection == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(collection);
    }

    @PostMapping("/addComic")
    public ResponseEntity<PersonalCollectionDatabase> addComicToCollection(
            @RequestBody CreateUserComicRequestBody requestModel) {
        Long userId = requestModel.getUserId();
        Long comicId = requestModel.getComicId();

        log.info("Adding comic with id: " + comicId + " to collection for user with id: " + userId);

        PersonalCollectionDatabase collection = personalCollectionService.addComicToCollection(userId, comicId);

        if (collection == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(collection);
    }

}
