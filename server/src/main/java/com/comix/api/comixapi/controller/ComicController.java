package com.comix.api.comixapi.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.comix.api.comixapi.model.comic.ComicBook;
import com.comix.api.comixapi.model.creator.Creator;
import com.comix.api.comixapi.repository.ComicRepository;
import com.comix.api.comixapi.repository.CreatorRepository;

@RestController
@RequestMapping("/comics")
public class ComicController {
    // create a log
    private static final Logger log = LoggerFactory.getLogger(ComicController.class);

    private final ComicRepository comicRepository;
    private final CreatorRepository creatorRepository;

    public ComicController(ComicRepository repository, CreatorRepository creatorRepository) {
        this.comicRepository = repository;
        this.creatorRepository = creatorRepository;
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
