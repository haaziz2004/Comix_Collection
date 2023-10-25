package com.comix.api.comixapi.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comix.api.comixapi.model.comic.ComicBook;
import com.comix.api.comixapi.model.comic.IComic;
import com.comix.api.comixapi.model.creator.Creator;
import com.comix.api.comixapi.repository.CharacterRepository;
import com.comix.api.comixapi.repository.ComicRepository;
import com.comix.api.comixapi.repository.CreatorRepository;
import com.comix.api.comixapi.repository.UserRepository;
import com.comix.api.comixapi.requestbody.ComicSearchRequestBody;
import com.comix.api.comixapi.requestbody.ComicUpdateRequestBody;
import com.comix.api.comixapi.requestbody.CreateComicRequestBody;

import com.comix.api.comixapi.model.search.SearchResults;
import com.comix.api.comixapi.model.search.SortByPublicationDate;
import com.comix.api.comixapi.model.user.User;
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

    public List<IComic> getAllComics() {
        List<IComic> comics = new ArrayList<>(comicRepository.findAllByUserIdIsNull());

        SearchResults comicSorter = new SearchResults(comics);

        comicSorter.doSort();

        return comicSorter.getSearchResults();
    }

    public ComicBook updateComic(Long id, ComicUpdateRequestBody body) {
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
        String value = body.getValue();
        String grade = body.getGrade();
        String slabbed = body.getSlabbed();
        String creators = body.getCreators();
        String principleCharacters = body.getPrincipleCharacters();

        // check which fields aren't null and update them
        if (storyTitle != null) {
            comic.setStoryTitle(storyTitle);
        }
        if (publisher != null && !publisher.equals("")) {
            comic.setPublisher(publisher);
        }
        if (!issueNumber.equals("") && issueNumber != null) {
            comic.setIssueNumber(issueNumber);
        }
        comic.setDescription(description);

        if (!seriesTitle.equals("") && seriesTitle != null) {
            comic.setSeriesTitle(seriesTitle);
        }
        if (!volumeNumber.equals("") && volumeNumber != null) {
            comic.setVolumeNumber(volumeNumber);
        }
        if (!publicationDate.equals("") && publicationDate != null) {
            comic.setPublicationDate(publicationDate);
        }
        if (value != null) {
            comic.setValue(Double.parseDouble(value));
        }
        if (grade != null) {
            comic.setGrade(Integer.parseInt(grade));
        }
        if (slabbed != null) {
            comic.setSlabbed(Boolean.parseBoolean(slabbed));
        }
        if (creators != null) {
            // split creators by comma
            String[] creatorsArr = creators.split(",");
            if (creatorsArr.length == 0) {
                return null;
            }
            for (String creatorName : creatorsArr) {
                if (creatorName.equals("")) {
                    continue;
                }
                Creator creator = creatorRepository.findByName(creatorName.trim());

                if (creator == null) {
                    creator = new Creator(creatorName.trim());
                    creator = creatorRepository.save(creator);
                }

                comic.addCreator(creator);
            }
        }
        if (principleCharacters != null) {
            // split principle characters by comma
            String[] charactersArr = principleCharacters.split(",");
            if (charactersArr.length == 0) {
                return null;
            }
            for (String characterName : charactersArr) {
                if (characterName.equals("")) {
                    continue;
                }
                Character character = characterRepository.findByName(characterName.trim());

                if (character == null) {
                    character = new Character(characterName.trim());
                    character = characterRepository.save(character);
                }

                comic.addCharacter(character);
            }
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
        comicBook.setStoryTitle(storyTitle);

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

    public List<IComic> searchComics(String queryString, ComicSearchRequestBody.SearchType searchType,
            ComicSearchRequestBody.SortType sortType) {
        // Any search terms should be matched against any of the following fields:
        // series title, principle characters, creator names, description
        Set<IComic> comics = new HashSet<>();

        if (searchType == ComicSearchRequestBody.SearchType.EXACT) {
            comics.addAll(comicRepository.findAllBySeriesTitle(queryString));
            comics.addAll(comicRepository.findAllByDescription(queryString));
            List<Character> character = characterRepository.findAllByName(queryString);
            for (Character c : character)
                comics.addAll(comicRepository.findAllByPrincipleCharacters(c));

            List<Creator> creator = creatorRepository.findAllByName(queryString);
            for (Creator c : creator)
                comics.addAll(comicRepository.findAllByCreators(c));
        } else if (searchType == ComicSearchRequestBody.SearchType.PARTIAL) {
            comics.addAll(comicRepository.findAllBySeriesTitleContaining(queryString));
            comics.addAll(comicRepository.findAllByDescriptionContaining(queryString));

            for (Character c : characterRepository.findAllByNameContaining(queryString))
                comics.addAll(comicRepository.findAllByPrincipleCharacters(c));
            for (Creator c : creatorRepository.findAllByNameContaining(queryString))
                comics.addAll(comicRepository.findAllByCreators(c));
        }

        if (comics.isEmpty()) {
            return null;
        }

        SearchResults comicSorter = new SearchResults(new ArrayList<>(comics));

        if (sortType == ComicSearchRequestBody.SortType.DATE) {
            comicSorter.setSorter(new SortByPublicationDate());
        }

        comicSorter.doSort();

        return comicSorter.getSearchResults();
    }

    public ComicBook createAndAddComicToUser(Long userId, CreateComicRequestBody body) {
        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            return null;
        }

        ComicBook comic = createComic(body);

        if (comic == null) {
            return null;
        }

        comic.setUser(user);
        comic.setSlabbed(false);
        comic.setGrade(0);
        comic.setValue(0);
        comicRepository.save(comic);

        user.addComic(comic);
        userRepository.save(user);

        return comic;
    }
}
