package com.example.board.common.exception;

import lombok.Getter;

@Getter
public class UnauthorizedException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "인증되지 않은 사용자입니다.";

    public UnauthorizedException() {
        super(DEFAULT_MESSAGE);
    }

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}
