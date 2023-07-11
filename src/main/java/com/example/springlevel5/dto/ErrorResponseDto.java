package com.example.springlevel5.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;

@Getter
public class ErrorResponseDto {

    LocalDateTime timestamp;
    HttpStatus status;
    String error;
    String path;

    ErrorResponseDto(ErrorResponseDtoBuilder builder) {
        timestamp = builder.timestamp;
        status = builder.status;
        error = builder.error;
        path = builder.path;
    }

    public static ErrorResponseDtoBuilder builder(Integer status, String error) {
        return new ErrorResponseDtoBuilder(status, error);
    }
    public static ErrorResponseDtoBuilder builder(HttpStatus status, String error) {
        return new ErrorResponseDtoBuilder(status, error);
    }

    public static class ErrorResponseDtoBuilder {

        // 필수 인자
        private final HttpStatus status;
        private final String error;

        // 선택 인자 (default 값 셋팅
        LocalDateTime timestamp = LocalDateTime.now();
        String path = "/";
        private ErrorResponseDtoBuilder(){
            this.status = null;
            this.error = null;
        }

        public ErrorResponseDtoBuilder(Integer status, String error) {
            this.status = HttpStatus.valueOf(status);
            this.error = error;
        }

        public ErrorResponseDtoBuilder(HttpStatus status, String error) {
            this.status = status;
            this.error = error;
        }

        public ErrorResponseDtoBuilder timestamp(LocalDateTime date) {
            this.timestamp = date;
            return this;
        }

        public ErrorResponseDtoBuilder path(String path) {
            this.path = path;
            return this;
        }

        public ErrorResponseDto build() {
            return new ErrorResponseDto(this);
        }
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

