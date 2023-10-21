package com.comix.comixbackend.comic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TestValue {
    @Test
    public void testNoValue() {
        IComic noValue = new ComicBook("Publisher", "HI", 1, 1, "Octoberish");
        System.out.println("Checked if class is created with no value and as a base");
        assertEquals(0.0, noValue.getValue());
    }

    @Test
    public void testNormalValue() {
        IComic value = new ComicValueDecorator(new ComicBook("Publisher", "HI", 1, 1, "Octoberish"));
        System.out.println("Checked if class is created with value");
        assertEquals(5, value.getValue());
    }

    @Test
    public void testGradedValue() {
        IComic graded = new ComicGradedDecorator(
                new ComicValueDecorator(new ComicBook("Publisher", "HI", 1, 1, "Octoberish")));
        System.out.println("Checked if class is created with a value 3 times the count");
        assertEquals(15, graded.getValue());
    }

    @Test
    public void testSlabbedValue() {
        IComic slabbed = new ComicSlabbedDecorator(
                new ComicGradedDecorator(
                        new ComicValueDecorator(new ComicBook("Publisher", "HI", 1, 1, "Octoberish"))));
        System.out.println("Checked if class is created with a value double that of graded");
        assertEquals(30, slabbed.getValue());
    }
}
