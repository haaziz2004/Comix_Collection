package com.comix.api.comixapi.service;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.comix.api.comixapi.controller.ComicController;
import com.comix.api.comixapi.model.character.Character;
import com.comix.api.comixapi.model.comic.ComicBook;
import com.comix.api.comixapi.model.comic.IComic;
import com.comix.api.comixapi.model.creator.Creator;
import com.comix.api.comixapi.model.search.SearchResults;
import com.comix.api.comixapi.model.search.SortByPublicationDate;
import com.comix.api.comixapi.model.user.User;
import com.comix.api.comixapi.repository.CharacterRepository;
import com.comix.api.comixapi.repository.ComicRepository;
import com.comix.api.comixapi.repository.CreatorRepository;
import com.comix.api.comixapi.repository.UserRepository;
import com.comix.api.comixapi.requestbody.ComicSearchRequestBody;
import com.comix.api.comixapi.requestbody.ComicUpdateRequestBody;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

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

    @Value("classpath:comics.csv")
    private Resource csvResource;

    private static final Logger log = LoggerFactory.getLogger(ComicService.class);

    public ComicBook getComicById(Long comicId) {
        return comicRepository.findById(comicId).orElse(null);
    }

    public List<IComic> getAllComics(int size, int page) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ComicBook> comicPage = comicRepository.findAllByUserIdIsNull(pageable);
        List<IComic> comics = new ArrayList<>(comicPage.getContent());

        SearchResults comicSorter = new SearchResults(comics);
        comicSorter.doSort();

        return comicSorter.getSearchResults();
    }

    public ComicBook updateComic(Long id, ComicUpdateRequestBody body) {
        ComicBook comic = comicRepository.findById(id).orElse(null);

        if (comic == null) {
            return null;
        }

        String publisher = body.getPublisher().trim();
        String seriesTitle = body.getSeriesTitle().trim();
        String volumeNumber = body.getVolumeNumber().trim();
        String issueNumber = body.getIssueNumber().trim();
        String publicationDate = body.getPublicationDate().trim();
        String storyTitle = body.getStoryTitle();
        String description = body.getDescription();
        String value = body.getValue();
        String grade = body.getGrade();
        String slabbed = body.getSlabbed();
        String creators = body.getCreators();
        String principleCharacters = body.getPrincipleCharacters();

        // check which fields aren't null and update them
        if (storyTitle != null && !storyTitle.equals("")) {
            comic.setStoryTitle(storyTitle.trim());
        }
        if (publisher != null && !publisher.equals("")) {
            comic.setPublisher(publisher);
        }
        if (issueNumber != null && issueNumber.equals("")) {
            comic.setIssueNumber(issueNumber);
        }

        if (description != null && !description.equals("")) {
            comic.setDescription(description.trim());
        }

        if (seriesTitle != null && !seriesTitle.equals("")) {
            comic.setSeriesTitle(seriesTitle);
        }
        if (volumeNumber != null && !volumeNumber.equals("")) {
            comic.setVolumeNumber(volumeNumber);
        }
        if (publicationDate != null && !publicationDate.equals("")) {
            comic.setPublicationDate(publicationDate);
        }
        if (value != null && !value.equals("")) {
            comic.setValue(Double.parseDouble(value.trim()));
        }
        if (grade != null && !grade.equals("")) {
            comic.setGrade(Integer.parseInt(grade.trim()));
        }
        if (slabbed != null && !slabbed.equals("")) {
            comic.setSlabbed(Boolean.parseBoolean(slabbed.trim()));
        }
        if (creators != null && !creators.equals("")) {
            // split creators by comma
            String[] creatorsArr = creators.trim().split(",");
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
        if (principleCharacters != null && !principleCharacters.equals("")) {
            // split principle characters by comma
            String[] charactersArr = principleCharacters.trim().split(",");
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

        try {
            comic = comicRepository.save(comic);
        } catch (Exception e) {
            return null;
        }

        return comic;
    }

    public ComicBook createComic(ComicUpdateRequestBody body) {
        String publisher = body.getPublisher().trim();
        String seriesTitle = body.getSeriesTitle().trim();
        String volumeNumber = body.getVolumeNumber().trim();
        String issueNumber = body.getIssueNumber().trim();
        String publicationDate = body.getPublicationDate().trim();
        String storyTitle = body.getStoryTitle();
        String description = body.getDescription();
        String value = body.getValue();
        String grade = body.getGrade();
        String slabbed = body.getSlabbed();
        String creators = body.getCreators();
        String principleCharacters = body.getPrincipleCharacters();

        ComicBook comic = new ComicBook(publisher, seriesTitle, volumeNumber, issueNumber, publicationDate);

        if (storyTitle != null && !storyTitle.equals("")) {
            comic.setPublisher(publisher.trim());
        }

        if (description != null && !description.equals("")) {
            comic.setDescription(description.trim());
        }

        if (value != null && !value.equals("")) {
            comic.setValue(Double.parseDouble(value.trim()));
        }
        if (grade != null && !grade.equals("")) {
            comic.setGrade(Integer.parseInt(grade.trim()));
        }
        if (slabbed != null && !slabbed.equals("")) {
            comic.setSlabbed(Boolean.parseBoolean(slabbed.trim()));
        }
        if (creators != null && !creators.equals("")) {
            // split creators by comma
            String[] creatorsArr = creators.trim().split(",");
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
        if (principleCharacters != null && !principleCharacters.equals("")) {
            // split principle characters by comma
            String[] charactersArr = principleCharacters.trim().split(",");
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

        try {
            comic = comicRepository.save(comic);
        } catch (Exception e) {
            return null;
        }

        return comic;
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

    public ComicBook createAndAddComicToUser(Long userId, ComicUpdateRequestBody body) {
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

    public Set<ComicBook> pupulatedb() {
        Set<ComicBook> comics = new HashSet<>();

        try {
            FileReader fileReader = new FileReader("./src/main/resources/comics.csv");
            CSVReader csvReader = new CSVReaderBuilder(fileReader).withCSVParser(new CSVParserBuilder()
                    .withQuoteChar('"')
                    .build()).build();
            String[] line;

            while ((line = csvReader.readNext()) != null) {
                String seriesTitle = line[0];
                String volumeNumber = "1";
                if (seriesTitle.contains(", Vol.")) {
                    String[] split = seriesTitle.split(", Vol.");
                    seriesTitle = split[0].trim();
                    volumeNumber = split[1].trim();
                }
                String issueNumber = line[1];
                String storyTitle = line[2];
                String description = line[3];
                String publisher = line[4];
                String publicationDate = line[5];
                String[] creatorNames = line[8].split("\\|");

                Set<Creator> creators = new HashSet<>();

                for (String creatorName : creatorNames) {
                    creators.add(new Creator(creatorName.trim()));
                }

                if (seriesTitle.equals("") || volumeNumber.equals("") ||
                        issueNumber.equals("") || publisher.equals("")
                        || publicationDate.equals("")) {
                    continue;
                }

                ComicBook comic = new ComicBook(publisher.trim(), seriesTitle.trim(), volumeNumber.trim(),
                        issueNumber.trim(), publicationDate.trim());

                if (!creators.isEmpty()) {
                    comic.setCreators(creators);
                }

                if (!storyTitle.equals("")) {
                    comic.setStoryTitle(storyTitle.trim());
                }

                if (!description.equals("")) {
                    comic.setDescription(description.trim());
                }

                comics.add(comic);
            }

            fileReader.close();

            // Now 'comics' contains the parsed comic book objects with separate creators.
        } catch (Exception e) {
            e.printStackTrace();
        }

        // List<ComicBook> comicList = new ArrayList<>(comics);
        List<ComicBook> comicList = new ArrayList<>(comics);

        for (int i = 0; i < comicList.size(); i++) {
            log.info("Saving " + i + " out of " + comicList.size());
            ComicBook comic = comicList.get(i);
            Set<Creator> creators = comic.getCreators();

            try {
                for (Creator creator : creators) {
                    Creator creatordb = creatorRepository.findByName(creator.getName());
                    if (creatordb == null) {
                        creatorRepository.save(creator);
                    } else {
                        comic.removeCreator(creator);
                        comic.addCreator(creatordb);
                    }
                }
                comicRepository.save(comic);

            } catch (Exception e) {
            }

            if (i % 100 == 0) {
                creatorRepository.flush();
            }

        }

        return comics;
    }
}
