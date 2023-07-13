package com.example.springlevel5.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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