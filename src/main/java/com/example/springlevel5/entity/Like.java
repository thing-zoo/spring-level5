package com.example.springlevel5.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@MappedSuperclass
@NoArgsConstructor
public abstract class Like {

    @Id
    @Column(name = "like_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    public Like(User user) {
        this.userId = user.getId();
    }

    public boolean checkUser(User user){
        return this.userId == user.getId();
    }
}
