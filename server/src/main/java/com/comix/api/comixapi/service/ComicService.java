package com.comix.api.comixapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comix.api.comixapi.model.comic.ComicBook;
import com.comix.api.comixapi.model.creator.Creator;
import com.comix.api.comixapi.repository.CharacterRepository;
import com.comix.api.comixapi.repository.ComicRepository;
import com.comix.api.comixapi.repository.CreatorRepository;
import com.comix.api.comixapi.repository.UserRepository;
import com.comix.api.comixapi.requestbody.CreateComicRequestBody;
import com.comix.api.comixapi.model.character.Character;

@Service
public class ComicService {
    @Autowired
    private ComicRepository comicRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CreatorRepository creatorRepository;
    @Autowired
    private CharacterRepository characterRepository;

    public ComicBook getComicById(Long comicId) {
        return comicRepository.findById(comicId).orElse(null);
    }

    public List<ComicBook> getAllComics() {
        return comicRepository.findAllByUserIdIsNull();
    }

    public ComicBook updateComic(Long id, CreateComicRequestBody body) {
        ComicBook comic = comicRepository.findById(id).orElse(null);

        if (comic == null) {
            return null;
        }

        String storyTitle = body.getStoryTitle();
        String publisher = body.getPublisher();
        String issueNumber = body.getIssueNumber();
        String description = body.getDescription();
        String seriesTitle = body.getSeriesTitle();
        String volumeNumber = body.getVolumeNumber();
        String publicationDate = body.getPublicationDate();

        // check which fields aren't null and update them
        if (storyTitle != null) {
            comic.setStoryTitle(storyTitle);
        }
        if (publisher != null) {
            comic.setPublisher(publisher);
        }
        if (issueNumber != null) {
            comic.setIssueNumber(issueNumber);
        }
        if (description != null) {
            comic.setDescription(description);
        }
        if (seriesTitle != null) {
            comic.setSeriesTitle(seriesTitle);
        }
        if (volumeNumber != null) {
            comic.setVolumeNumber(volumeNumber);
        }
        if (publicationDate != null) {
            comic.setPublicationDate(publicationDate);
        }

        return comicRepository.save(comic);
    }

    public ComicBook createComic(CreateComicRequestBody body) {
        String storyTitle = body.getStoryTitle();
        String publisher = body.getPublisher();
        String issueNumber = body.getIssueNumber();
        String description = body.getDescription();
        String seriesTitle = body.getSeriesTitle();
        String volumeNumber = body.getVolumeNumber();
        String publicationDate = body.getPublicationDate();

        ComicBook comicBook = new ComicBook(publisher, seriesTitle, volumeNumber, issueNumber, publicationDate);
        comicBook.setDescription(description);
        comicBook.setSeriesTitle(storyTitle);

        try {
            comicBook = comicRepository.save(comicBook);
        } catch (Exception e) {
            return null;
        }

        return comicBook;
    }

    public ComicBook addCreatorToComic(Long comicId, String name) {
        ComicBook comic = comicRepository.findById(comicId).orElse(null);

        if (comic == null) {
            return null;
        }

        Creator creator = creatorRepository.findByName(name);

        if (creator == null) {
            creator = new Creator(name);
            creator = creatorRepository.save(creator);
        }

        comic.addCreator(creator);

        return comicRepository.save(comic);
    }

    public ComicBook addCharacterToComic(Long comicId, String name) {
        ComicBook comic = comicRepository.findById(comicId).orElse(null);

        if (comic == null) {
            return null;
        }

        Character character = characterRepository.findByName(name);

        if (character == null) {
            character = new Character(name);
            character = characterRepository.save(character);
        }

        comic.addCharacter(character);

        return comicRepository.save(comic);
    }

    public ComicBook addGradeToComic(Long comicId, int grade) {
        ComicBook comic = comicRepository.findById(comicId).orElse(null);

        if (comic == null) {
            return null;
        }

        comic.setGrade(grade);

        return comicRepository.save(comic);
    }

    public ComicBook addValueToComic(Long comicId, double value) {
        ComicBook comic = comicRepository.findById(comicId).orElse(null);

        if (comic == null) {
            return null;
        }

        comic.setValue(value);

        return comicRepository.save(comic);
    }

    public ComicBook addSlabbedToComic(Long comicId, Boolean slabbed) {
        ComicBook comic = comicRepository.findById(comicId).orElse(null);

        if (comic == null) {
            return null;
        }

        comic.setSlabbed(slabbed);

        return comicRepository.save(comic);
    }
}
