package com.example.springlevel5.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CategoryRequestDto {
    @NotBlank
    String name;
}
