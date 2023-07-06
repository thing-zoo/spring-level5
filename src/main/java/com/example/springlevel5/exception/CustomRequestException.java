package com.example.springlevel5.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomRequestException extends RuntimeException{
    HttpStatus status;

    public CustomRequestException(String message) {
        this(HttpStatus.BAD_REQUEST, message);
    }

    public CustomRequestException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
}
