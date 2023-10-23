package com.comix.api.comixapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.comix.api.comixapi.model.comic.ComicBook;
import com.comix.api.comixapi.repository.ComicRepository;
import com.comix.api.comixapi.repository.PersonalCollectionRepository;
import com.comix.api.comixapi.repository.UserComicRepository;
import com.comix.api.comixapi.repository.UserRepository;

public class ComicService {
    @Autowired
    private ComicRepository comicRepository;
    @Autowired
    private PersonalCollectionRepository collectionRepository;
    @Autowired
    private UserComicRepository userComicRepository;
    @Autowired
    private UserRepository userRepository;

    public ComicBook getComicById(Long comicId) {
        return comicRepository.findById(comicId).orElse(null);
    }

    public List<ComicBook> getAllComics() {
        return comicRepository.findAll();
    }
}
