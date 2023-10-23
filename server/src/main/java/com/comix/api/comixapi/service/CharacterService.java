package com.comix.api.comixapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comix.api.comixapi.repository.CharacterRepository;
import com.comix.api.comixapi.repository.ComicRepository;
import com.comix.api.comixapi.repository.CreatorRepository;
import com.comix.api.comixapi.repository.UserRepository;
import com.comix.api.comixapi.model.character.Character;

@Service
public class CharacterService {
    @Autowired
    private CharacterRepository characterRepository;
    @Autowired
    private ComicRepository comicRepository;
    @Autowired
    private CreatorRepository creatorRepository;
    @Autowired
    private UserRepository userRepository;

    public Character createCharacter(String name) {
        // check if character exists
        Character character = characterRepository.findByName(name);

        if (character != null) {
            return null;
        }

        character = new Character(name);

        return characterRepository.save(character);
    }

}
