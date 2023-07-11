package com.example.springlevel5.controller;

import com.example.springlevel5.dto.CategoryRequestDto;
import com.example.springlevel5.security.UserDetailsImpl;
import com.example.springlevel5.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    @PostMapping("/categories")
    public ResponseEntity<?> createCategory(@RequestBody @Valid CategoryRequestDto requestDto,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return categoryService.createCategory(requestDto, userDetails.getUser());
    }

}
