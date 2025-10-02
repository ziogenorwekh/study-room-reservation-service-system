package com.choongang.studyreservesystem.exception;

// 삭체 권한이 없을 경우 (관리자/게시글 작성자만 삭제 가능)
public class UnauthorizedDeleteException extends RuntimeException {

    public UnauthorizedDeleteException() { super(); }

    public UnauthorizedDeleteException(String message) {
        super(message);
    }
}
