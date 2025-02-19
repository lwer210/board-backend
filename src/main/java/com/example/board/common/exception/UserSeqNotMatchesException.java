package com.example.board.common.exception;

public class UserSeqNotMatchesException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "UserSeq not matches";

    public UserSeqNotMatchesException() {
        super(DEFAULT_MESSAGE);
    }

    public UserSeqNotMatchesException(String message) {
        super(message);
    }

    public UserSeqNotMatchesException(String message, Throwable cause) {
        super(message, cause);
    }
}
