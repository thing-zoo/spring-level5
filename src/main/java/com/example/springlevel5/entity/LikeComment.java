package com.example.springlevel5.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class LikeComment extends Like {

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    public LikeComment(Comment comment, User user) {
        super(user);
        this.comment = comment;
    }
}
