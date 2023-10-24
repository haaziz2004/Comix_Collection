package com.comix.api.comixapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.comix.api.comixapi.model.comic.ComicBook;

@Repository
public interface ComicRepository extends CrudRepository<ComicBook, Long> {
    List<ComicBook> findAllByUserIdIsNull();

    List<ComicBook> findAllByUserId(Long userId);
}
