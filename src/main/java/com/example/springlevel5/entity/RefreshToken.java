package com.example.springlevel5.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {
    @Id
    private Long userId;

    private String refreshToken;

    public RefreshToken(Long userId) {
        this.userId = userId;
        this.refreshToken = UUID.randomUUID().toString();;
    }

    public String changeToken() {
        this.refreshToken = UUID.randomUUID().toString();
        return this.refreshToken;
    }

}
