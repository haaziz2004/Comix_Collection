package com.comix.api.comixapi.model.comic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.comix.api.comixapi.model.user.User;

public class TestValue {
    @Test
    public void testNoValue() {
        IComic noValue = new ComicBook("Publisher", "HI", "1", "1", "Octoberish", new User("test", "test"), 0.0, 0,
                false);
        System.out.println("Checked if class is created with no value and as a base");
        assertEquals(0.0, noValue.getValue());
    }

    @Test
    public void testGradedValue() {
        IComic graded_1 = new ComicGradedDecorator(
                new ComicBook("Publisher", "HI", "1", "1", "Octoberish", new User("test", "test"), 1.0, 0, false));
        System.out.println("Checked if class is created with a value 3 times the count");
        IComic graded_2 = new ComicGradedDecorator(
                new ComicBook("Publisher", "HI", "1", "1", "Octoberish", new User("test", "test"), 1.0, 5, false));
        System.out.println("Checked if class is created with a value 3 times the count");
        assertEquals(1, graded_1.getValue());
        assertEquals(5, graded_2.getValue());
    }

    @Test
    public void testSlabbedValue() {
        IComic slabbed = new ComicSlabbedDecorator(
                new ComicGradedDecorator(new ComicBook("Publisher", "HI", "1", "1", "Octoberish",
                        new User("test", "test"), 1.0, 1, false)));
        System.out.println("Checked if class is created with a value double that of graded");
        assertEquals(2, slabbed.getValue());
    }
}
