package com.example.board.common.exception;

public class EmptyFileException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "파일이 비어있습니다.";

    public EmptyFileException() {
        super(DEFAULT_MESSAGE);
    }

    public EmptyFileException(String message) {
        super(message);
    }

    public EmptyFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
