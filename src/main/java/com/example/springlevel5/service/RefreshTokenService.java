package com.example.springlevel5.service;

import com.example.springlevel5.entity.RefreshToken;
import com.example.springlevel5.entity.User;
import com.example.springlevel5.jwt.JwtUtil;
import com.example.springlevel5.repository.RefreshRepository;
import com.example.springlevel5.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshRepository refreshRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public String saveDatabaseToken(Long id){
        Optional<RefreshToken> refreshToken = refreshRepository.findById(id);
        if(refreshToken.isPresent()){ // 이미 발행 됐으면 갱신
            RefreshToken refreshTokenValue = refreshToken.get();
            String changeToken = refreshTokenValue.changeToken();
            refreshRepository.save(refreshTokenValue);
            return changeToken;
        }
        else { // 발행 한 적이 없으면 신규 생성
            RefreshToken refreshTokenValue = new RefreshToken(id);
            refreshRepository.save(refreshTokenValue);
            return refreshTokenValue.getRefreshToken();
        }
    }

    // DB 리프레쉬 토큰에서 유저 정보 찾아오기
    public User readDatabaseToken(String token){
        RefreshToken refreshToken = refreshRepository.findByRefreshToken(token).orElseThrow(() -> new IllegalArgumentException("not find token"));
        User user = userRepository.findById(refreshToken.getUserId()).orElseThrow(() -> new IllegalArgumentException("not find user"));

        return user;
    }

    // Filter에서 리프레쉬 토큰 발행 전용 메서드
    public void createRefreshToken(User user, HttpServletResponse response){
        String token = saveDatabaseToken(user.getId());
        token = jwtUtil.createTokenSetSubject(token);
        jwtUtil.addJwtToCookieWithHeaderName(token, JwtUtil.REFRESH_TOKEN_HEADER, response);
    }
}
