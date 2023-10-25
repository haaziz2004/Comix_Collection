package com.comix.api.comixapi.model.search;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.comix.api.comixapi.model.comic.ComicBook;
import com.comix.api.comixapi.model.comic.IComic;

public class TestSort {

    @Test
    public void sortByTitle() { // issue numbers and volume numbers contains no letters
        ComicBook comic1 = new ComicBook("Marvel", "ManSpider", "3", "1", "1999");
        ComicBook comic2 = new ComicBook("Marvel", "ManSpider", "1", "2", "2000");
        ComicBook comic3 = new ComicBook("Marvel", "ManSpider", "2", "1", "2000");
        ComicBook comic4 = new ComicBook("Marvel", "ManSpider", "2", "2", "2000");
        List<IComic> comic_list = new ArrayList<IComic>();
        comic_list.add(comic1);
        comic_list.add(comic2);
        comic_list.add(comic3);
        comic_list.add(comic4);
        SearchResults comicsSorter = new SearchResults(comic_list);
        comicsSorter.doSort();

        List<IComic> sortedComics = comicsSorter.getSearchResults();
        assertEquals(sortedComics.get(0), comic2);
        assertEquals(sortedComics.get(1), comic3);
        assertEquals(sortedComics.get(2), comic4);
        assertEquals(sortedComics.get(3), comic1);

    }

    @Test
    public void sortByTitleAnnual() { // issue numbers and volume numbers contains annuals
        ComicBook comic1 = new ComicBook("Marvel", "ManSpider", "3", "1", "1999");
        ComicBook comic2 = new ComicBook("Marvel", "ManSpider", "1", "2", "2000");
        ComicBook comic3 = new ComicBook("Marvel", "ManSpider", "Annual", "1A", "2000");
        ComicBook comic4 = new ComicBook("Marvel", "ManSpider", "Annual", "1B", "2000");
        List<IComic> comic_list = new ArrayList<IComic>();
        comic_list.add(comic1);
        comic_list.add(comic2);
        comic_list.add(comic3);
        comic_list.add(comic4);
        SearchResults comicsSorter = new SearchResults(comic_list);
        comicsSorter.doSort();

        List<IComic> sortedComics = comicsSorter.getSearchResults();
        assertEquals(sortedComics.get(0), comic2);
        assertEquals(sortedComics.get(1), comic1);
        assertEquals(sortedComics.get(2), comic3);
        assertEquals(sortedComics.get(3), comic4);

    }

    @Test
    public void sortByTitleMix() { // issue numbers and volume numbers contains letters
        ComicBook comic1 = new ComicBook("Marvel", "SpiderMan", "3A", "1", "1999");
        ComicBook comic2 = new ComicBook("Marvel", "SpiderMan", "3B", "1", "2000");
        ComicBook comic3 = new ComicBook("Marvel", "ManSpider", "2", "1", "2000");
        ComicBook comic4 = new ComicBook("Marvel", "ManSpider", "2", "2", "2000");
        List<IComic> comic_list = new ArrayList<IComic>();
        comic_list.add(comic1);
        comic_list.add(comic2);
        comic_list.add(comic3);
        comic_list.add(comic4);
        SearchResults comicsSorter = new SearchResults(comic_list);
        comicsSorter.doSort();

        List<IComic> sortedComics = comicsSorter.getSearchResults();
        assertEquals(sortedComics.get(0), comic3);
        assertEquals(sortedComics.get(1), comic4);
        assertEquals(sortedComics.get(2), comic1);
        assertEquals(sortedComics.get(3), comic2);

    }

    @Test
    public void sortByPublicationDateYear() {
        ComicBook comic1 = new ComicBook("Marvel", "ManSpider", "3", "1", "1880");
        ComicBook comic2 = new ComicBook("Marvel", "ManSpider", "1", "2", "2000");
        ComicBook comic3 = new ComicBook("Marvel", "ManSpider", "2", "1", "1995");
        ComicBook comic4 = new ComicBook("Marvel", "ManSpider", "2", "2", "2008");
        List<IComic> comic_list = new ArrayList<IComic>();
        comic_list.add(comic1);
        comic_list.add(comic2);
        comic_list.add(comic3);
        comic_list.add(comic4);
        SearchResults comicsSorter = new SearchResults(comic_list);
        comicsSorter.setSorter(new SortByPublicationDate());
        comicsSorter.doSort();

        List<IComic> sortedComics = comicsSorter.getSearchResults();
        assertEquals(sortedComics.get(0), comic1);
        assertEquals(sortedComics.get(1), comic3);
        assertEquals(sortedComics.get(2), comic2);
        assertEquals(sortedComics.get(3), comic4);
    }

    @Test
    public void sortByPublicationDateMix() { // mix of date formats
        ComicBook comic1 = new ComicBook("Marvel", "ManSpider", "vol 3", "1", "Jan 5, 1880");
        ComicBook comic2 = new ComicBook("Marvel", "ManSpider", "vol 1", "2", "Dec 25, 1880");
        ComicBook comic3 = new ComicBook("Marvel", "ManSpider", "vol 2", "1", "1995");
        ComicBook comic4 = new ComicBook("Marvel", "ManSpider", "vol 2", "2", "Jan, 1880");
        List<IComic> comic_list = new ArrayList<IComic>();
        comic_list.add(comic1);
        comic_list.add(comic2);
        comic_list.add(comic3);
        comic_list.add(comic4);
        SearchResults comicsSorter = new SearchResults(comic_list);
        comicsSorter.setSorter(new SortByPublicationDate());
        comicsSorter.doSort();

        List<IComic> sortedComics = comicsSorter.getSearchResults();
        assertEquals(sortedComics.get(0), comic4);
        assertEquals(sortedComics.get(1), comic1);
        assertEquals(sortedComics.get(2), comic2);
        assertEquals(sortedComics.get(3), comic3);
    }
}