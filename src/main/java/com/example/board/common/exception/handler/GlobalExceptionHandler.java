package com.example.board.common.exception.handler;

import com.example.board.common.exception.*;
import com.example.board.common.exception.response.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {
            NewPasswordNotMatchesException.class
    })
    public ResponseEntity<ExceptionResponse> handleNewPasswordNotMatchesException(NewPasswordNotMatchesException e) {
        ExceptionResponse response = ExceptionResponse.builder()
                .message(e.getMessage())
                .code(HttpStatus.BAD_REQUEST.value())
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {
            AlreadyRegisteredEmailException.class,
            AlreadyRegisteredNicknameException.class
    })
    public ResponseEntity<ExceptionResponse> alreadyExceptionHandler(Exception e){
        ExceptionResponse response = ExceptionResponse.builder()
                .message(e.getMessage())
                .code(HttpStatus.CONFLICT.value())
                .build();

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {
            UserNotFoundException.class,
            TokenNotFoundException.class,
            ArticleNotFoundException.class
    })
    public ResponseEntity<ExceptionResponse> userNotFoundExceptionHandler(Exception e){
        ExceptionResponse response = ExceptionResponse.builder()
                .message(e.getMessage())
                .code(HttpStatus.NOT_FOUND.value())
                .build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {
            EmailSendFailedException.class,
            ParseTokenException.class
    })
    public ResponseEntity<ExceptionResponse> emailSendFailedExceptionHandler(Exception e){
        ExceptionResponse response = ExceptionResponse.builder()
                .message(e.getMessage())
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {
            ExpiredTokenException.class,
            NotContainsRoleException.class,
            UnauthorizedException.class
    })
    public ResponseEntity<ExceptionResponse> expiredTokenExceptionHandler(Exception e){
        ExceptionResponse response = ExceptionResponse.builder()
                .message(e.getMessage())
                .code(HttpStatus.UNAUTHORIZED.value())
                .build();

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {
            Exception.class
    })
    public ResponseEntity<ExceptionResponse> exceptionHandler(Exception e){
        ExceptionResponse response = ExceptionResponse.builder()
                .message(e.getMessage())
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
