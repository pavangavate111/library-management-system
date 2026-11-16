package com.library.lms.exception;

public class BorrowingNotFoundException extends RuntimeException {

    public BorrowingNotFoundException(String message) {
        super(message);
    }
}