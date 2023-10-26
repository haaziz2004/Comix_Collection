package com.comix.api.comixapi.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.comix.api.comixapi.model.comic.ComicBook;
import com.comix.api.comixapi.model.creator.Creator;
import com.comix.api.comixapi.model.character.Character;

@Repository
public interface ComicRepository extends JpaRepository<ComicBook, Long> {
    List<ComicBook> findAllByUserIdIsNull();

    Page<ComicBook> findAllByUserIdIsNull(Pageable page);

    List<ComicBook> findAllByUserId(Long userId);

    List<ComicBook> findAllBySeriesTitle(String queryString);

    List<ComicBook> findAllByDescription(String queryString);

    List<ComicBook> findAllByPrincipleCharacters(Character character);

    List<ComicBook> findAllByCreators(Creator creator);

    List<ComicBook> findAllBySeriesTitleContaining(String queryString);

    List<ComicBook> findAllByDescriptionContaining(String queryString);

    List<ComicBook> findAllByPrincipleCharactersContaining(Character character);

    List<ComicBook> findAllByCreatorsContaining(Creator creator);

}
