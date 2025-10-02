package com.choongang.studyreservesystem.exception;

// 삭제 처리 중 오류가 발생했을 경우 발생하는 에러
public class BoardDeletionException extends RuntimeException {

    public BoardDeletionException() { super(); }

    public BoardDeletionException(String message) { super(message); }

    public BoardDeletionException(String message, Throwable cause) {
        super(message, cause);
    }
}
