package com.example.board.common.exception;

public class FileNotFoundException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "파일을 찾을 수 없습니다.";

    public FileNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public FileNotFoundException(String message) {
        super(message);
    }

    public FileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
