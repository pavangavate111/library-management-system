package com.library.lms.exception;

public class BorrowingLimitExceededException extends RuntimeException {

    public BorrowingLimitExceededException(String message) {
        super(message);
    }
}