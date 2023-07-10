package com.example.springlevel5.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;

@Entity
@Getter
@ToString
@NoArgsConstructor
public class LikePost extends Like {

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public LikePost(Post post, User user) {
        super(user);
        this.post = post;
    }
}