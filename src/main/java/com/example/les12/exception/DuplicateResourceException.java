package com.example.les12.exception;

public class DuplicateResourceException extends RuntimeException {
    // private static final long serialVersionUID = 1L; - Wat doet dit?

    public DuplicateResourceException() {
        super();
    }

    public DuplicateResourceException(String message) {
        super(message);
    }
}

