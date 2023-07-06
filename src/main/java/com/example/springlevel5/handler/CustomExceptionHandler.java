package com.example.springlevel5.handler;

import com.example.springlevel5.dto.ErrorResponseDto;
import com.example.springlevel5.exception.CustomRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgumentException(IllegalArgumentException exception) {
        ErrorResponseDto responseDto = ErrorResponseDto.builder(HttpStatus.BAD_REQUEST, exception.getMessage()).build();

        return ResponseEntity.status(responseDto.getStatus()).body(responseDto);
    }

    @ExceptionHandler(MissingRequestCookieException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgumentException(MissingRequestCookieException exception) {
        ErrorResponseDto responseDto = ErrorResponseDto.builder(HttpStatus.BAD_REQUEST, "토큰이 유효하지 않습니다.").build();
        return ResponseEntity.status(responseDto.getStatus()).body(responseDto);
    }

    @ExceptionHandler(CustomRequestException.class)
    public ResponseEntity<ErrorResponseDto> handleCustomRequestException(CustomRequestException exception) {
        ErrorResponseDto responseDto = ErrorResponseDto.builder(exception.getStatus(), exception.getMessage()).build();
        return ResponseEntity.status(responseDto.getStatus()).body(responseDto);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationError(MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();

        StringBuilder builder = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            builder.append("[");
            builder.append(fieldError.getField());
            builder.append("](은)는 ");
            builder.append(fieldError.getDefaultMessage());
            builder.append(" 입력된 값: [");
            builder.append(fieldError.getRejectedValue());
            builder.append("]");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(builder.toString());
    }
}

