package com.example.board.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AlreadyRegisteredEmail extends RuntimeException{

    private static final String DEFAULT_MESSAGE = "이미 가입된 이메일입니다.";

    public AlreadyRegisteredEmail() {
        super(DEFAULT_MESSAGE);
    }

    public AlreadyRegisteredEmail(String message) {
        super(message);
    }

    public AlreadyRegisteredEmail(String message, Throwable cause) {
        super(message, cause);
    }
}
