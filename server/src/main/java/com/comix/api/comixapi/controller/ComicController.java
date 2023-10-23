package com.comix.api.comixapi.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.comix.api.comixapi.model.comic.ComicBook;
import com.comix.api.comixapi.repository.ComicRepository;
import com.comix.api.comixapi.requestbody.CreateComicRequestBody;

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
    public ResponseEntity<List<ComicBook>> getAllComics() {
        log.info("Getting all comics");
        List<ComicBook> comics = comicRepository.findAll();

        if (comics == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(comics);
    }

    @GetMapping("/byId/{id}")
    public ResponseEntity<ComicBook> getComicById(@PathVariable Long id) {
        log.info("Getting comic with id: " + id);
        ComicBook comic = comicRepository.findById(id).orElse(null);

        if (comic == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(comic);
    }

    @PostMapping("/create")
    public ResponseEntity<ComicBook> createComic(@RequestBody CreateComicRequestBody body) {
        String storyTitle = body.getStoryTitle();
        String publisher = body.getPublisher();
        String issueNumber = body.getIssueNumber();
        String description = body.getDescription();
        String seriesTitle = body.getSeriesTitle();
        String volumeNumber = body.getVolumeNumber();
        String publicationDate = body.getPublicationDate();

        log.info("Creating comic with series title: " + seriesTitle + " and publisher: " + publisher);

        ComicBook comicBook = new ComicBook(publisher, seriesTitle, volumeNumber, issueNumber, publicationDate);
        comicBook.setDescription(description);
        comicBook.setSeriesTitle(storyTitle);

        ComicBook comic = comicRepository.save(comicBook);

        if (comic == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(comic);
    }
}
