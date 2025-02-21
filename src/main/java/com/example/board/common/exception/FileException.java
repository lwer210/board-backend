package com.example.board.common.exception;

public class FileException extends RuntimeException{

    private static final String DEFAULT_MESSAGE = "파일 업로드에 실패하였습니다.";

    public FileException() {
        super(DEFAULT_MESSAGE);
    }

    public FileException(String message) {
        super(message);
    }

    public FileException(String message, Throwable cause) {
        super(message, cause);
    }
}
