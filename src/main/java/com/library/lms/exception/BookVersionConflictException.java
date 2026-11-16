package com.library.lms.exception;

public class BookVersionConflictException extends RuntimeException {

    public BookVersionConflictException(String message) {
        super(message);
    }
}