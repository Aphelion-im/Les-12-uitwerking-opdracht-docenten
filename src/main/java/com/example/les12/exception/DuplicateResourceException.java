package com.example.les12.exception;

public class DuplicateResourceException extends RuntimeException{
    public DuplicateResourceException(String message) {
        super(message);
    }
}

// throw new DuplicateResourceException("This resource already exists!");
