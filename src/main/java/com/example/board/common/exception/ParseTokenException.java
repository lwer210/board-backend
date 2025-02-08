package com.example.board.common.exception;

import lombok.Getter;

@Getter
public class ParseTokenException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "parse에 실패하였습니다.";

    public ParseTokenException() {
        super(DEFAULT_MESSAGE);
    }

    public ParseTokenException(String message) {
        super(message);
    }

    public ParseTokenException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }

    public ParseTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
