package com.comix.api.comixapi.exceptions;

public class ComicNotFoundException extends RuntimeException {
    public ComicNotFoundException(Long id) {
        super("Could not find comic with id: " + id);
    }
}
