package com.example.board.common.exception;

import lombok.Getter;

@Getter
public class NotContainsRoleException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "권한 정보가 없습니다.";

    public NotContainsRoleException() {
        super(DEFAULT_MESSAGE);
    }

    public NotContainsRoleException(String message) {
        super(message);
    }

    public NotContainsRoleException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }
}
