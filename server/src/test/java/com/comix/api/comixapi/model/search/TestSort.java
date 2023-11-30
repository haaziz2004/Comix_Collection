package com.comix.api.comixapi.model.search;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.comix.api.comixapi.model.comic.Comic;
import com.comix.api.comixapi.model.comic.IComic;

public class TestSort {

        @Test
        public void sortByTitle() { // issue numbers and volume numbers contains no letters
                IComic comic1 = new Comic(1l, "Marvel", "ManSpider", "3", "1",
                                new Timestamp(System.currentTimeMillis()),
                                "storyTitle", "description");
                IComic comic2 = new Comic(2l, "Marvel", "ManSpider", "1", "2",
                                new Timestamp(System.currentTimeMillis()),
                                "storyTitle", "description");
                IComic comic3 = new Comic(3l, "Marvel", "ManSpider", "2", "1",
                                new Timestamp(System.currentTimeMillis()),
                                "storyTitle", "description");
                IComic comic4 = new Comic(4l, "Marvel", "ManSpider", "2", "2",
                                new Timestamp(System.currentTimeMillis()),
                                "storyTitle", "description");
                List<IComic> comic_list = new ArrayList<IComic>();
                comic_list.add(comic2);
                comic_list.add(comic1);
                comic_list.add(comic4);
                comic_list.add(comic3);
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
                IComic comic1 = new Comic(1l, "Marvel", "ManSpider", "3", "1",
                                new Timestamp(System.currentTimeMillis()),
                                "storyTitle", "description");
                IComic comic2 = new Comic(2l, "Marvel", "ManSpider", "1", "2",
                                new Timestamp(System.currentTimeMillis()),
                                "storyTitle", "description");
                IComic comic3 = new Comic(3l, "Marvel", "ManSpider", "Annual", "1A",
                                new Timestamp(System.currentTimeMillis()),
                                "storyTitle", "description");
                IComic comic4 = new Comic(4l, "Marvel", "ManSpider", "Annual", "1B",
                                new Timestamp(System.currentTimeMillis()),
                                "storyTitle", "description");
                List<IComic> comic_list = new ArrayList<IComic>();
                comic_list.add(comic2);
                comic_list.add(comic1);
                comic_list.add(comic4);
                comic_list.add(comic3);
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
                IComic comic1 = new Comic(1l, "Marvel", "SpiderMan", "3A", "1",
                                new Timestamp(System.currentTimeMillis()),
                                "storyTitle", "description");
                IComic comic2 = new Comic(2l, "Marvel", "SpiderMan", "3B", "1",
                                new Timestamp(System.currentTimeMillis()),
                                "storyTitle", "description");
                IComic comic3 = new Comic(3l, "Marvel", "ManSpider", "2", "1",
                                new Timestamp(System.currentTimeMillis()),
                                "storyTitle", "description");
                IComic comic4 = new Comic(4l, "Marvel", "ManSpider", "2", "2",
                                new Timestamp(System.currentTimeMillis()),
                                "storyTitle", "description");
                List<IComic> comic_list = new ArrayList<IComic>();
                comic_list.add(comic2);
                comic_list.add(comic1);
                comic_list.add(comic4);
                comic_list.add(comic3);
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
                Comic comic1 = new Comic(1l, "Marvel", "ManSpider", "3", "1",
                                new Timestamp(new Calendar.Builder().setDate(1880, 1, 1).build().getTimeInMillis()),
                                "storyTitle",
                                "description");
                Comic comic2 = new Comic(2l, "Marvel", "ManSpider", "1", "2",
                                new Timestamp(new Calendar.Builder().setDate(1995, 1, 1).build().getTimeInMillis()),
                                "storyTitle",
                                "description");
                Comic comic3 = new Comic(3l, "Marvel", "ManSpider", "2", "1",
                                new Timestamp(new Calendar.Builder().setDate(2000, 1, 1).build().getTimeInMillis()),
                                "storyTitle",
                                "description");
                Comic comic4 = new Comic(4l, "Marvel", "ManSpider", "2", "2",
                                new Timestamp(new Calendar.Builder().setDate(2008, 1, 1).build().getTimeInMillis()),
                                "storyTitle",
                                "description");
                List<IComic> comic_list = new ArrayList<IComic>();
                comic_list.add(comic2);
                comic_list.add(comic1);
                comic_list.add(comic4);
                comic_list.add(comic3);
                SearchResults comicsSorter = new SearchResults(comic_list);
                comicsSorter.setSorter(new SortByPublicationDate());
                comicsSorter.doSort();

                List<IComic> sortedComics = comicsSorter.getSearchResults();
                assertEquals(sortedComics.get(0), comic4);
                assertEquals(sortedComics.get(1), comic3);
                assertEquals(sortedComics.get(2), comic2);
                assertEquals(sortedComics.get(3), comic1);
        }
}