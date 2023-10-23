package com.comix.api.comixapi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.comix.api.comixapi.service.CharacterService;
import com.comix.api.comixapi.service.PersonalCollectionService;

@RestController
@RequestMapping("/character")
public class CharacterController {
    private static final Logger log = LoggerFactory.getLogger(PersonalCollectionController.class);

    private final CharacterService characterService;

    public CharacterController(CharacterService characterService) {
        this.characterService = characterService;
    }

}
