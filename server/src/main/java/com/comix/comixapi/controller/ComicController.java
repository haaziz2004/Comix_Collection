package com.comix.comixapi.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.comix.comixapi.model.comic.ComicBook;
import com.comix.comixapi.model.comic.Creator;
import com.comix.comixapi.repository.ComicRepository;
import com.comix.comixapi.repository.CreatorRepository;

@RestController
@RequestMapping("/comic")
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
    public List<ComicBook> findAll() {
        log.info("Find all comics");
        List<ComicBook> comics = comicRepository.findAll();
        log.info("Coimcs" + comics);
        return comics;
    }

    @GetMapping("/create")
    public void createComic() {
        try {
            Set<Creator> creators = new HashSet<Creator>();
            Creator creator = new Creator("Bob Rozakis");
            creators.add(creator);
            creatorRepository.save(creator);
            comicRepository.save(new ComicBook("DC Comics", "'Mazing Man", 4, 1, "Jan, 1986", creators, null, null));
        } catch (Exception e) {
            log.error("Error creating comic", e);
        }
    }
}
