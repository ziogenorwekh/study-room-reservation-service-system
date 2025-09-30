package com.choongang.studyreservesystem.exception;

public class UnauthorizedDeleteException extends RuntimeException {

    public UnauthorizedDeleteException() { super(); }

    public UnauthorizedDeleteException(String message) {
        super(message);
    }
}
