package com.example.board.common.exception;

import lombok.Getter;

@Getter
public class ExpiredTokenException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "만료된 토큰입니다.";

    public ExpiredTokenException() {
        super(DEFAULT_MESSAGE);
    }

    public ExpiredTokenException(String message) {
        super(message);
    }

    public ExpiredTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
