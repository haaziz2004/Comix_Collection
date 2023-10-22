package com.comix.api.comixapi.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.comix.api.comixapi.model.comic.ComicBook;
import com.comix.api.comixapi.repository.ComicRepository;

@RestController
@RequestMapping("/comics")
public class ComicController {
    // create a log
    private static final Logger log = LoggerFactory.getLogger(ComicController.class);

    private final ComicRepository comicRepository;

    public ComicController(ComicRepository repository) {
        this.comicRepository = repository;
    }

    @GetMapping("/all")
    public List<ComicBook> getAllComics() {
        log.info("Getting all comics");
        return comicRepository.findAll();
    }

    @GetMapping("/byId/{id}")
    public ComicBook getComicById(@PathVariable Long id) {
        log.info("Getting comic by id");
        return comicRepository.findById(id).orElseThrow();
    }
}
