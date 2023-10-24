package com.comix.api.comixapi.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.comix.api.comixapi.model.creator.Creator;

@Repository
public interface CreatorRepository extends CrudRepository<Creator, Long> {
    Creator findByName(String name);

    List<Creator> findAllByNameContaining(String name);

    List<Creator> findAllByName(String name);
}
