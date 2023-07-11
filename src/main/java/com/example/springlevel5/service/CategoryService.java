package com.example.springlevel5.service;

import com.example.springlevel5.dto.CategoryRequestDto;
import com.example.springlevel5.dto.ErrorResponseDto;
import com.example.springlevel5.entity.Category;
import com.example.springlevel5.entity.User;
import com.example.springlevel5.exception.CustomResponseException;
import com.example.springlevel5.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public ResponseEntity<ErrorResponseDto> createCategory(CategoryRequestDto requestDto, User user) {
        // 사용자별 카테고리 중복 확인
        Optional<Category> checkCategory = categoryRepository.findByUser_IdAndName(user.getId(), requestDto.getName());
        if (checkCategory.isPresent()) {
            throw new CustomResponseException(HttpStatus.BAD_REQUEST, "이미 존재하는 카테고리입니다.");
        }

        Category category = Category.builder()
                .name(requestDto.getName())
                .user(user)
                .build();
        categoryRepository.save(category);

        return ResponseEntity.ok(
                ErrorResponseDto.builder(201,"카테고리 생성 성공: " + category.getName())
                        .build()
        );
    }
}
