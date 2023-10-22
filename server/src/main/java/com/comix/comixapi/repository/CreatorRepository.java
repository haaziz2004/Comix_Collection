package com.comix.comixapi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.comix.comixapi.model.comic.Creator;

@Repository
public interface CreatorRepository extends CrudRepository<Creator, Long> {

}
