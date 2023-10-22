package com.comix.api.comixapi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.comix.api.comixapi.model.character.Character;

@Repository
public interface CharacterRepository extends CrudRepository<Character, Long> {

}
