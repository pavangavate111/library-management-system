package com.library.lms.exception;

public class DuplicateMemberEmailException extends RuntimeException {

    public DuplicateMemberEmailException(String message) {
        super(message);
    }
}