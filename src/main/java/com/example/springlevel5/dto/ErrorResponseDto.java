package com.example.springlevel5.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ErrorResponseDto {

    LocalDateTime timestamp;
    Long status;
    String error;
    String path;

    @Builder
    public ErrorResponseDto(Long status, String error, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.path = path;
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

