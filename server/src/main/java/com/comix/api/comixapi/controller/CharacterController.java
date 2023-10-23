package com.comix.api.comixapi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.comix.api.comixapi.requestbody.CreateCharacterRequestBody;
import com.comix.api.comixapi.service.CharacterService;
import com.comix.api.comixapi.model.character.Character;

@RestController
@RequestMapping("/character")
public class CharacterController {
    private static final Logger log = LoggerFactory.getLogger(CharacterController.class);

    private final CharacterService characterService;

    public CharacterController(CharacterService characterService) {
        this.characterService = characterService;
    }

    @PostMapping("/create")
    public ResponseEntity<Character> createCharacter(@RequestBody CreateCharacterRequestBody body) {
        String name = body.getName();
        log.info("Creating character with name: {}", name);

        Character character = characterService.createCharacter(name);

        if (character == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(character);
    }

}
