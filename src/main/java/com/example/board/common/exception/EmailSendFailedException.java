package com.example.board.common.exception;

import lombok.Getter;

@Getter
public class EmailSendFailedException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "이메일 발송에 실패하였습니다.";

    public EmailSendFailedException(){
        super(DEFAULT_MESSAGE);
    }

    public EmailSendFailedException(String message){
        super(message);
    }

    public EmailSendFailedException(String message, Throwable cause){
        super(message, cause);
    }
}
