package com.comix.api.comixapi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.comix.api.comixapi.model.usercomic.UserComic;
import com.comix.api.comixapi.requestbody.CreateUserComicRequestBody;
import com.comix.api.comixapi.service.UserComicService;

@RestController
@RequestMapping("/users/comics")
public class UserComicController {
    private static final Logger log = LoggerFactory.getLogger(UserComicController.class);

    private final UserComicService userComicService;

    public UserComicController(UserComicService userComicService) {
        this.userComicService = userComicService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createUserComic(@RequestBody CreateUserComicRequestBody requestModel) {
        UserComic userComic = userComicService.createUserComic((requestModel.getComicId()), (requestModel.getUserId()));

        if (userComic == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok("User comic created and associated with the user.");
    }
}
