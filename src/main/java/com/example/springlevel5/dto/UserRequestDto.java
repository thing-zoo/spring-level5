package com.example.springlevel5.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UserRequestDto {
    @NotBlank
    @Pattern(regexp = "^[a-z0-9]{4,10}$", message = "아이디는 소문자와 숫자 포함 4~10이내로 입력해주세요.")
    private String username;

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,15}$",
            message = "비밀번호는 대소문자와 숫자, 특수문자 포함 8~15자리 이내로 입력해주세요.")
    private String password;

    private boolean admin = false;
    private String adminToken = "";
}

