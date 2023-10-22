package com.comix.api.comixapi.model.comic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.comix.api.comixapi.model.user.User;
import com.comix.api.comixapi.model.usercomic.UserComic;

public class TestValue {
    @Test
    public void testNoValue() {
        IComic noValue = new UserComic(new User("Test", "test@gmail.com", "password"),
                new ComicBook("Publisher", "HI", 1, 1, "Octoberish"));
        System.out.println("Checked if class is created with no value and as a base");
        assertEquals(0.0, noValue.getValue());
    }

    @Test
    public void testGradedValue() {
        IComic graded = new ComicGradedDecorator(new UserComic(new User("Test", "test@gmail.com", "password"),
                new ComicBook("Publisher", "HI", 1, 1, "Octoberish"), 1));
        System.out.println("Checked if class is created with a value 3 times the count");
        assertEquals(3, graded.getValue());
    }

    @Test
    public void testSlabbedValue() {
        IComic slabbed = new ComicSlabbedDecorator(
                new ComicGradedDecorator(new UserComic(new User("Test", "test@gmail.com", "password"),
                        new ComicBook("Publisher", "HI", 1, 1, "Octoberish"), 1)));
        System.out.println("Checked if class is created with a value double that of graded");
        assertEquals(6, slabbed.getValue());
    }
}
