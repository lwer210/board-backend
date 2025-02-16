package com.example.board.common.exception;

public class ArticleNotFoundException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "게시글을 찾을 수 없습니다.";

    public ArticleNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public ArticleNotFoundException(String message) {
        super(message);
    }

    public ArticleNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
