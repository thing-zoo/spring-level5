package com.example.springlevel5.jwt;

import com.example.springlevel5.entity.User;
import com.example.springlevel5.security.UserDetailsImpl;
import com.example.springlevel5.security.UserDetailsServiceImpl;
import com.example.springlevel5.service.RefreshTokenService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.example.springlevel5.jwt.JwtUtil.REFRESH_TOKEN_HEADER;

@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final RefreshTokenService refreshTokenService;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService, RefreshTokenService refreshTokenService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {

        String tokenValue = jwtUtil.getTokenFromRequest(req);

        if (StringUtils.hasText(tokenValue)) {
            // JWT 토큰 substring
            tokenValue = jwtUtil.substringToken(tokenValue);
            log.info(tokenValue);

            if (!jwtUtil.validateToken(tokenValue)) {
                log.error("Token Error");
                return;
            }

            Claims info = jwtUtil.getUserInfoFromToken(tokenValue);

            try {
                setAuthentication(info.getSubject());
            } catch (Exception e) {
                log.error(e.getMessage());
                return;
            }
        } else {
            try {
                RefreshTokenValidation(req, res);
            } catch (Exception e) {
                log.error(e.getMessage());
                return;
            }
        }

        filterChain.doFilter(req, res);
    }

    private void RefreshTokenValidation(HttpServletRequest req, HttpServletResponse res) throws Exception{
        // Refresh Token
        String tokenValue = jwtUtil.getTokenFromHeaderTokenName(req, REFRESH_TOKEN_HEADER);
        if (StringUtils.hasText(tokenValue)) {

            // JWT 토큰 substring
            tokenValue = jwtUtil.substringToken(tokenValue);
            log.info(tokenValue);

            if (!jwtUtil.validateToken(tokenValue)) {
                log.error("Token Error");
                throw new Exception("Token Error");
            }

            Claims info = jwtUtil.getUserInfoFromToken(tokenValue);
            log.info(info.getSubject());

            User user = refreshTokenService.readDatabaseToken(info.getSubject());

            try {
                setAuthenticationRefreshToken(user);
                // AccessToken 재 발행
                String token = jwtUtil.createToken(user.getUsername(), user.getRole());
                jwtUtil.addJwtToCookie(token, res);
                // RefreshToken 재 발행
                refreshTokenService.createRefreshToken(user, res);
            } catch (Exception e) {
                log.error(e.getMessage());
                throw new Exception(e.getMessage());
            }
        }
    }


    // 인증 처리
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // User 인증 처리 추가
    public void setAuthenticationRefreshToken(User user) {
        UserDetailsImpl userDetailsImpl = new UserDetailsImpl(user);
        UserDetails userDetails = userDetailsImpl;
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}