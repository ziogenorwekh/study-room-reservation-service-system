package com.choongang.studyreservesystem.exception;

// 게시글이 존재하지 않을 경우 삭제 시도 시 발생하는 에러
public class BoardNotFoundException extends RuntimeException {

    public BoardNotFoundException() { super(); }

    public BoardNotFoundException(String message) { super(message); }

    public BoardNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
