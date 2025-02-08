package com.example.board.common.exception;

import lombok.Getter;

@Getter
public class AlreadyRegisteredNickname extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "이미 가입된 닉네임입니다.";

    public AlreadyRegisteredNickname() {
        super(DEFAULT_MESSAGE);
    }

    public AlreadyRegisteredNickname(String message) {
        super(message);
    }

    public AlreadyRegisteredNickname(String message, Throwable cause) {
        super(message, cause);
    }
}
