package com.comix.api.comixapi.model.comic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TestValue {
    @Test
    public void testNoValue() {
        IComic noValue = new ComicBook("Publisher", "HI", 1, 1, "Octoberish");
        System.out.println("Checked if class is created with no value and as a base");
        assertEquals(1.0, noValue.getValue());
    }

    @Test
    public void testGradedValue() {
        IComic graded = new ComicGradedDecorator(
                new ComicBook("Publisher", "HI", 1, 1, "Octoberish"));
        System.out.println("Checked if class is created with a value 3 times the count");
        assertEquals(3, graded.getValue());
    }

    @Test
    public void testSlabbedValue() {
        IComic slabbed = new ComicSlabbedDecorator(
                new ComicGradedDecorator(new ComicBook("Publisher", "HI", 1, 1, "Octoberish")));
        System.out.println("Checked if class is created with a value double that of graded");
        assertEquals(6, slabbed.getValue());
    }
}
