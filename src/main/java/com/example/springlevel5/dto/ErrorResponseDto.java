package com.example.springlevel5.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;

@Getter
@Builder(builderMethodName = "innerBuilder")
public class ErrorResponseDto {

    LocalDateTime timestamp;
    HttpStatus status;
    String error;
    String path;


    public static ErrorResponseDtoBuilder builder(Integer status, String error) {
        return innerBuilder().timestamp(LocalDateTime.now())
                .status(HttpStatus.valueOf(status))
                .error(error);
    }
    public static ErrorResponseDtoBuilder builder(HttpStatus status, String error) {
        return innerBuilder().timestamp(LocalDateTime.now())
                .status(status)
                .error(error);
    }
    public static ErrorResponseDtoBuilder builder(HttpStatusCode status, String error) {
        return innerBuilder().timestamp(LocalDateTime.now())
                .status(HttpStatus.valueOf(status.value()))
                .error(error);
    }
}
/*
{
    "timestamp": "2023-07-04T07:47:03.710+00:00",
    "status": 400,
    "error": "Bad Request",
    "path": "/api/posts/1/comment"
}
 */

