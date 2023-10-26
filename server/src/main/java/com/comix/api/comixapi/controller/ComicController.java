package com.comix.api.comixapi.controller;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
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
import com.comix.api.comixapi.model.creator.Creator;
import com.comix.api.comixapi.repository.ComicRepository;
import com.comix.api.comixapi.requestbody.ComicAddCreatorRequestBody;
import com.comix.api.comixapi.requestbody.ComicSearchRequestBody;
import com.comix.api.comixapi.requestbody.ComicUpdateRequestBody;
import com.comix.api.comixapi.service.ComicService;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

@RestController
@RequestMapping("/comics")
public class ComicController {
    // create a log
    private static final Logger log = LoggerFactory.getLogger(ComicController.class);

    private final ComicService comicService;
    private final ComicRepository comicRepository;

    public ComicController(ComicService comicService, ComicRepository comicRepository) {
        this.comicService = comicService;
        this.comicRepository = comicRepository;
    }

    @GetMapping("/all")
    public ResponseEntity<List<IComic>> getAllComics() {
        log.info("Getting all comics");

        List<IComic> comics = comicService.getAllComics();

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
    public ResponseEntity<ComicBook> createComic(@RequestBody ComicUpdateRequestBody body) {
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

    @PostMapping("/create/{userId}")
    public ResponseEntity<ComicBook> createComicForUser(@PathVariable Long userId,
            @RequestBody ComicUpdateRequestBody body) {
        log.info("Creating comic for user with id: " + userId);

        ComicBook comic = comicService.createAndAddComicToUser(userId, body);

        if (comic == null) {
            // comic already exists
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(comic);
    }

    @GetMapping("/populate/db")
    public ResponseEntity<List<ComicBook>> populateDatabase() {
        Set<ComicBook> comics = comicService.pupulatedb();
        // Set<ComicBook> comics = new HashSet<>();

        // try {
        // FileReader fileReader = new FileReader("./src/main/resources/comics.csv");
        // CSVReader csvReader = new
        // CSVReaderBuilder(fileReader).withSkipLines(1).build(); // Skip the header
        // line
        // String[] line;

        // while ((line = csvReader.readNext()) != null) {
        // String seriesTitle = line[0];
        // String volumeNumber = "1";
        // if (seriesTitle.contains(", Vol.")) {
        // String[] split = seriesTitle.split(", Vol.");
        // seriesTitle = split[0].trim();
        // volumeNumber = split[1].trim();
        // }
        // String issueNumber = line[1];
        // String storyTitle = line[2];
        // String description = line[3];
        // String publisher = line[4];
        // String publicationDate = line[5];
        // String[] creatorNames = line[8].split("\\|");

        // Set<Creator> creators = new HashSet<>();
        // for (String creatorName : creatorNames) {
        // creators.add(new Creator(creatorName.trim()));
        // }

        // if (seriesTitle.equals("") || volumeNumber.equals("") ||
        // issueNumber.equals("") || publisher.equals("")
        // || publicationDate.equals("")) {
        // continue;
        // }

        // ComicBook comic = new ComicBook(publisher, seriesTitle, volumeNumber,
        // issueNumber, publicationDate);

        // if (!creators.isEmpty()) {
        // comic.setCreators(creators);
        // }

        // if (!storyTitle.equals("")) {
        // comic.setStoryTitle(storyTitle);
        // }

        // if (!description.equals("")) {
        // comic.setDescription(description);
        // }

        // comics.add(comic);
        // }

        // fileReader.close();

        // // Now 'comics' contains the parsed comic book objects with separate
        // creators.
        // } catch (Exception e) {
        // e.printStackTrace();
        // }

        // log.info("Done parsing CSV file");

        // List<ComicBook> comicList = new ArrayList<>(comics);

        // for (int i = 0; i < comics.size(); i++) {
        // log.info("Saving " + i);
        // ComicBook comic = comicList.get(i);
        // comicRepository.save(comic);

        // if (i % 100 == 0) {
        // comicRepository.flush();
        // }

        // }

        log.info("Comics size" + comics.size());

        return ResponseEntity.ok(comics.stream().toList());
    }
}
