package com.example.board.common.exception;

import lombok.Getter;

@Getter
public class TokenNotFoundException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "토큰을 찾을 수 없습니다.";

    public TokenNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public TokenNotFoundException(String message) {
        super(message);
    }

    public TokenNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
