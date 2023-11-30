package com.comix.api.comixapi.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.comix.api.comixapi.exceptions.ComicNotFoundException;
import com.comix.api.comixapi.exceptions.ConflictException;
import com.comix.api.comixapi.exceptions.UserNotFoundException;
import com.comix.api.comixapi.model.comic.Comic;
import com.comix.api.comixapi.model.comic.IComic;
import com.comix.api.comixapi.model.search.SearchResults;
import com.comix.api.comixapi.model.search.SearchSorter;
import com.comix.api.comixapi.model.search.SortByPublicationDate;
import com.comix.api.comixapi.model.search.SortByTitle;
import com.comix.api.comixapi.requestbody.ComicAddComicToPersonalCollectionRequestBody;
import com.comix.api.comixapi.requestbody.ComicSearchRequestBody;
import com.comix.api.comixapi.service.ComicService;

@RestController
@RequestMapping("/api/comics")
public class ComicController {
    @Autowired
    private ComicService comicService;

    private static final Logger log = LoggerFactory.getLogger(ComicController.class);

    @RequestMapping("/getAll")
    public ResponseEntity<List<Comic>> getAll() {
        log.info("Getting all comics");
        List<Comic> comics = comicService.getAll();

        if (comics.isEmpty()) {
            log.info("No comics found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(comics);
    }

    @RequestMapping("/getById/{id}")
    public ResponseEntity<Comic> getById(@PathVariable String id) {
        log.info("Getting comic with id: {}", id);
        Comic comic = comicService.getById(Long.parseLong(id));

        if (comic == null) {
            log.info("No comic found with id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(comic);
    }

    @PostMapping("/search")
    public ResponseEntity<List<IComic>> search(@RequestBody ComicSearchRequestBody comicSearchRequest) {
        log.info("Searching for comics with search request: {}", comicSearchRequest);
        List<IComic> comics = comicService.search(comicSearchRequest);

        if (comics.isEmpty()) {
            log.info("No comics found with search request: {}", comicSearchRequest);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(comics);
    }

    @PostMapping("/addComicToPersonalCollection")
    public ResponseEntity<Boolean> addComicToPersonalCollection(
            @RequestBody ComicAddComicToPersonalCollectionRequestBody comicAddComicToPersonalCollectionRequestBody)
            throws Exception {
        log.info("Adding comic with id: {} to personal collection for user with id: {}",
                comicAddComicToPersonalCollectionRequestBody.getComicId(),
                comicAddComicToPersonalCollectionRequestBody.getUserId());
        try {
            boolean success = comicService.addComicToPersonalCollection(
                    comicAddComicToPersonalCollectionRequestBody.getUserId(),
                    comicAddComicToPersonalCollectionRequestBody.getComicId());

            if (!success) {
                log.info("Failed to add comic with id: {} to personal collection for user with id: {}",
                        comicAddComicToPersonalCollectionRequestBody.getComicId(),
                        comicAddComicToPersonalCollectionRequestBody.getUserId());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

            return ResponseEntity.ok(true);
        } catch (ConflictException e) {
            log.info("Failed to add comic with id: {} to personal collection for user with id: {}",
                    comicAddComicToPersonalCollectionRequestBody.getComicId(),
                    comicAddComicToPersonalCollectionRequestBody.getUserId());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (ComicNotFoundException e) {
            log.info("Failed to add comic with id: {} to personal collection for user with id: {}",
                    comicAddComicToPersonalCollectionRequestBody.getComicId(),
                    comicAddComicToPersonalCollectionRequestBody.getUserId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (UserNotFoundException e) {
            log.info("Failed to add comic with id: {} to personal collection for user with id: {}",
                    comicAddComicToPersonalCollectionRequestBody.getComicId(),
                    comicAddComicToPersonalCollectionRequestBody.getUserId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.info("Failed to add comic with id: {} to personal collection for user with id: {}",
                    comicAddComicToPersonalCollectionRequestBody.getComicId(),
                    comicAddComicToPersonalCollectionRequestBody.getUserId());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
