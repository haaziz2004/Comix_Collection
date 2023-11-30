package com.comix.api.comixapi.model.comic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Timestamp;

import org.junit.jupiter.api.Test;

import com.comix.api.comixapi.model.collection.CollectionElement;
import com.comix.api.comixapi.model.user.User;

public class TestValue {

    @Test
    public void testNoValue() {
        CollectionElement comic = new UserComic(1l, "publisher", "seriesTitle", "volumeNumber", "issueNumber",
                new Timestamp(System.currentTimeMillis()), "storyTitle", "description");

        assertEquals(0.0, comic.getValue());
    }

    @Test
    public void testGradedValue() {
        CollectionElement comic = new UserComic(1l, "publisher", "seriesTitle", "volumeNumber", "issueNumber",
                new Timestamp(System.currentTimeMillis()), "storyTitle", "description", 10, 1, false);
        comic = new ComicGradedDecorator(comic);
        assertEquals(1, comic.getValue());

        comic = new UserComic(1l, "publisher", "seriesTitle", "volumeNumber", "issueNumber",
                new Timestamp(System.currentTimeMillis()), "storyTitle", "description", 10, 2, false);
        comic = new ComicGradedDecorator(comic);
        assertEquals(Math.log10(2) * 10, comic.getValue());

        comic = new UserComic(1l, "publisher", "seriesTitle", "volumeNumber", "issueNumber",
                new Timestamp(System.currentTimeMillis()), "storyTitle", "description", 10, 3, false);
        comic = new ComicGradedDecorator(comic);
        assertEquals(Math.log10(3) * 10, comic.getValue());

        comic = new UserComic(1l, "publisher", "seriesTitle", "volumeNumber", "issueNumber",
                new Timestamp(System.currentTimeMillis()), "storyTitle", "description", 10, 4, false);
        comic = new ComicGradedDecorator(comic);
        assertEquals(Math.log10(4) * 10, comic.getValue());

        comic = new UserComic(1l, "publisher", "seriesTitle", "volumeNumber", "issueNumber",
                new Timestamp(System.currentTimeMillis()), "storyTitle", "description", 10, 5, false);
        comic = new ComicGradedDecorator(comic);
        assertEquals(Math.log10(5) * 10, comic.getValue());

        comic = new UserComic(1l, "publisher", "seriesTitle", "volumeNumber", "issueNumber",
                new Timestamp(System.currentTimeMillis()), "storyTitle", "description", 10, 6, false);
        comic = new ComicGradedDecorator(comic);
        assertEquals(Math.log10(6) * 10, comic.getValue());

        comic = new UserComic(1l, "publisher", "seriesTitle", "volumeNumber", "issueNumber",
                new Timestamp(System.currentTimeMillis()), "storyTitle", "description", 10, 7, false);
        comic = new ComicGradedDecorator(comic);
        assertEquals(Math.log10(7) * 10, comic.getValue());

        comic = new UserComic(1l, "publisher", "seriesTitle", "volumeNumber", "issueNumber",
                new Timestamp(System.currentTimeMillis()), "storyTitle", "description", 10, 8, false);
        comic = new ComicGradedDecorator(comic);
        assertEquals(Math.log10(8) * 10, comic.getValue());

        comic = new UserComic(1l, "publisher", "seriesTitle", "volumeNumber", "issueNumber",
                new Timestamp(System.currentTimeMillis()), "storyTitle", "description", 10, 9, false);
        comic = new ComicGradedDecorator(comic);
        assertEquals(Math.log10(9) * 10, comic.getValue());

        comic = new UserComic(1l, "publisher", "seriesTitle", "volumeNumber", "issueNumber",
                new Timestamp(System.currentTimeMillis()), "storyTitle", "description", 10, 10, false);
        comic = new ComicGradedDecorator(comic);
        assertEquals(Math.log10(10) * 10, comic.getValue());
    }

    @Test
    public void testSlabbedValue() {
        CollectionElement comic = new UserComic(1l, "publisher", "seriesTitle", "volumeNumber", "issueNumber",
                new Timestamp(System.currentTimeMillis()), "storyTitle", "description", 10, 1, true);
        comic = new ComicSlabbedDecorator(comic);
        assertEquals(20, comic.getValue());

        comic = new UserComic(1l, "publisher", "seriesTitle", "volumeNumber", "issueNumber",
                new Timestamp(System.currentTimeMillis()), "storyTitle", "description", 20, 1, true);
        comic = new ComicSlabbedDecorator(comic);
        assertEquals(40, comic.getValue());
    }

    @Test
    public void testGradedAndSlabbedValue() {
        CollectionElement comic = new UserComic(1l, "publisher", "seriesTitle", "volumeNumber", "issueNumber",
                new Timestamp(System.currentTimeMillis()), "storyTitle", "description", 10, 1, true);
        comic = new ComicSlabbedDecorator(comic);
        comic = new ComicGradedDecorator(comic);
        assertEquals(2, comic.getValue());

        comic = new UserComic(1l, "publisher", "seriesTitle", "volumeNumber", "issueNumber",
                new Timestamp(System.currentTimeMillis()), "storyTitle", "description", 20, 1, true);
        comic = new ComicSlabbedDecorator(comic);
        comic = new ComicGradedDecorator(comic);
        assertEquals(4, comic.getValue());

        comic = new UserComic(1l, "publisher", "seriesTitle", "volumeNumber", "issueNumber",
                new Timestamp(System.currentTimeMillis()), "storyTitle", "description", 30, 5, true);
        comic = new ComicSlabbedDecorator(comic);
        comic = new ComicGradedDecorator(comic);
        assertEquals(Math.log10(5) * 30 * 2, comic.getValue());
    }
}