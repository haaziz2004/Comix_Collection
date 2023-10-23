package com.comix.api.comixapi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.comix.api.comixapi.model.collection.PersonalCollectionDatabase;
import com.comix.api.comixapi.model.collection.CollectionElement;

@Repository
public interface PersonalCollectionRepository extends CrudRepository<PersonalCollectionDatabase, Long> {

}
