package com.example.board.common.exception;

public class CommentNotFoundException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Comment not found";

    public CommentNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public CommentNotFoundException(String message) {
        super(message);
    }

    public CommentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
