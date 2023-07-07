package com.example.springlevel5.controller;

import com.example.springlevel5.dto.ErrorResponseDto;
import com.example.springlevel5.dto.UserRequestDto;
import com.example.springlevel5.security.UserDetailsImpl;
import com.example.springlevel5.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping("/users/signup")
    public ResponseEntity<ErrorResponseDto> signup(@RequestBody @Valid UserRequestDto requestDto) {
        return userService.signup(requestDto);
    }
    @DeleteMapping("/users")
    public ResponseEntity<ErrorResponseDto> deleteAccount(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.deleteAccount(userDetails);
    }
}
