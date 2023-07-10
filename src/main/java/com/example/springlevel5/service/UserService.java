package com.example.springlevel5.service;

import com.example.springlevel5.dto.ErrorResponseDto;
import com.example.springlevel5.dto.UserRequestDto;
import com.example.springlevel5.entity.User;
import com.example.springlevel5.entity.UserRoleEnum;
import com.example.springlevel5.exception.CustomResponseException;
import com.example.springlevel5.repository.UserRepository;
import com.example.springlevel5.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ADMIN_TOKEN: 관리자인지 확인하기 위한 토큰
    // 현업에서는 관리자 페이지나 승인자에 의해 결재하는 과정으로 함
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";
    public ResponseEntity<ErrorResponseDto> signup(UserRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new CustomResponseException(HttpStatus.BAD_REQUEST, "중복된 사용자가 존재합니다.");
        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }

        // 사용자 등록
        User user = new User(username, password, role);
        userRepository.save(user);

        ErrorResponseDto responseDto = ErrorResponseDto.builder(HttpStatus.CREATED.value(), "회원가입 성공")
                .build();

        return ResponseEntity.ok(responseDto);
    }

    public ResponseEntity<ErrorResponseDto> deleteAccount(UserDetailsImpl userDetails) {
        userRepository.delete(userDetails.getUser());
        ErrorResponseDto responseDto = ErrorResponseDto.builder(HttpStatus.OK.value(), "회원 삭제 완료")
                .build();
        return ResponseEntity.ok(responseDto);
    }

}
