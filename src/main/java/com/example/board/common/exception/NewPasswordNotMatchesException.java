package com.example.board.common.exception;

import lombok.Getter;

@Getter
public class NewPasswordNotMatchesException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "새 패스워드가 일치하지 않습니다.";

    public NewPasswordNotMatchesException() {
        super(DEFAULT_MESSAGE);
    }

    public NewPasswordNotMatchesException(String message) {
        super(message);
    }

    public NewPasswordNotMatchesException(String message, Throwable cause) {
        super(message, cause);
    }
}
