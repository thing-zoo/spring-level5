package com.example.springlevel5.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomResponseException extends RuntimeException{
    HttpStatus status;

    public CustomResponseException(String message) {
        this(HttpStatus.BAD_REQUEST, message);
    }

    public CustomResponseException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
}
