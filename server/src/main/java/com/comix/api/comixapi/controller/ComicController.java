package com.comix.api.comixapi.controller;

import java.util.List;
import java.util.Set;

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
import com.comix.api.comixapi.model.comic.IComic;
import com.comix.api.comixapi.requestbody.ComicAddCreatorRequestBody;
import com.comix.api.comixapi.requestbody.ComicSearchRequestBody;
import com.comix.api.comixapi.requestbody.ComicUpdateRequestBody;
import com.comix.api.comixapi.requestbody.CreateComicRequestBody;
import com.comix.api.comixapi.service.ComicService;

@RestController
@RequestMapping("/comics")
public class ComicController {
    // create a log
    private static final Logger log = LoggerFactory.getLogger(ComicController.class);

    private final ComicService comicService;

    public ComicController(ComicService comicService) {
        this.comicService = comicService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ComicBook>> getAllComics() {
        log.info("Getting all comics");

        List<ComicBook> comics = comicService.getAllComics();

        if (comics == null || comics.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(comics);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComicBook> getComicById(@PathVariable Long id) {
        log.info("Getting comic with id: " + id);

        ComicBook comic = comicService.getComicById(id);

        if (comic == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(comic);
    }

    @PostMapping("/create")
    public ResponseEntity<ComicBook> createComic(@RequestBody CreateComicRequestBody body) {
        log.info("Creating comic with story title: " + body.getStoryTitle());

        ComicBook comic = comicService.createComic(body);

        if (comic == null) {
            // comic already exists
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(comic);
    }

    @PostMapping("{id}/update")
    public ResponseEntity<ComicBook> updateComic(@PathVariable Long id, @RequestBody ComicUpdateRequestBody body) {
        log.info("Updating comic with id: " + id);

        ComicBook comic = comicService.updateComic(id, body);

        if (comic == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(comic);
    }

    @PostMapping("{comicId}/add/creator")
    public ResponseEntity<ComicBook> addCreatorToComic(@PathVariable Long comicId,
            @RequestBody ComicAddCreatorRequestBody body) {
        String creatorName = body.getCreatorName();

        log.info("Adding creator with name: " + creatorName + " to comic with id: " + comicId);

        ComicBook comic = comicService.addCreatorToComic(comicId, creatorName);

        if (comic == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(comic);
    }

    @PostMapping("{comicId}/add/character")
    public ResponseEntity<ComicBook> addCharacterToComic(@PathVariable Long comicId,
            @RequestBody ComicAddCreatorRequestBody body) {
        String characterName = body.getCreatorName();

        log.info("Adding character with name: " + characterName + " to comic with id: " + comicId);

        ComicBook comic = comicService.addCharacterToComic(comicId, characterName);

        if (comic == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(comic);
    }

    @PostMapping("{comicId}/grade/{grade}")
    public ResponseEntity<ComicBook> addGradeToComic(@PathVariable Long comicId, @PathVariable int grade) {
        log.info("Grading comic with id: " + comicId + " with grade: " + grade);

        ComicBook comic = comicService.addGradeToComic(comicId, grade);

        if (comic == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(comic);
    }

    @PostMapping("{comicId}/value/{value}")
    public ResponseEntity<ComicBook> addValueToComic(@PathVariable Long comicId, @PathVariable double value) {
        log.info("Adding value: " + value + " to comic with id: " + comicId);

        ComicBook comic = comicService.addValueToComic(comicId, value);

        if (comic == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(comic);
    }

    @PostMapping("{comicId}/slabbed/{slabbed}")
    public ResponseEntity<ComicBook> addSlabbedToComic(@PathVariable Long comicId, @PathVariable Boolean slabbed) {
        log.info("Adding slabbed: " + slabbed + " to comic with id: " + comicId);

        ComicBook comic = comicService.addSlabbedToComic(comicId, slabbed);

        if (comic == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(comic);
    }

    @PostMapping("/search")
    public ResponseEntity<List<IComic>> searchComics(@RequestBody ComicSearchRequestBody body) {
        String queryString = body.getQueryString();
        ComicSearchRequestBody.SearchType searchType = body.getSearchType();
        ComicSearchRequestBody.SortType sortType = body.getSortType();

        // if missing any fields then return bad request
        if (queryString == null || searchType == null || sortType == null) {
            return ResponseEntity.badRequest().build();
        }

        log.info("Searching for comics with query string: " + queryString + " and search type: " + searchType
                + " and sort type: " + sortType);

        List<IComic> comics = comicService.searchComics(queryString, searchType, sortType);

        if (comics == null || comics.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(comics);
    }
}
