package com.example.springlevel5.controller;

import com.example.springlevel5.dto.ErrorResponseDto;
import com.example.springlevel5.dto.UserRequestDto;
import com.example.springlevel5.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping("/users/signup")
    public ResponseEntity<ErrorResponseDto> signup(@RequestBody @Valid UserRequestDto requestDto) {
        return userService.signup(requestDto);
    }
}
