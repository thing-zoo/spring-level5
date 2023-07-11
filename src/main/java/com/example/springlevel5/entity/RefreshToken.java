package com.example.springlevel5.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class RefreshToken {
    @Id
    private String tokenName;

    @OneToOne
    private User user;
}
