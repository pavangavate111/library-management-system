package com.library.lms.exception;

public class DuplicateBorrowingException extends RuntimeException {

    public DuplicateBorrowingException(String message) {
        super(message);
    }
}