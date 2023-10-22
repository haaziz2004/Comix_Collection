package com.comix.comixapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.comix.comixapi.model.comic.ComicBook;

@Repository
public interface ComicRepository extends JpaRepository<ComicBook, Long> {

    List<ComicBook> findAll();
}
