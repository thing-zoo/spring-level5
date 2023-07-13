package com.example.springlevel5.entity;

import com.example.springlevel5.dto.CommentRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "comments")
public class Comment extends Timestamped {
    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
    private List<LikeComment> likes;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE)
    private List<Comment> children = new ArrayList<>();

    @Builder
    public Comment(String content, Post post, User user, Comment parent) {
        this.content = content;
        this.post = post;
        this.user = user;
        this.parent = parent;
        this.likes = new ArrayList<>();
    }

    public void update(CommentRequestDto requestDto) {
        this.content = requestDto.getContent();
    }

    public void addLike(LikeComment likeComment){
        this.likes.add(likeComment);
    }
    public void DeleteLike(LikeComment likeComment){
        this.likes.remove(likeComment);
    }

    public LikeComment changeLike(User user){
        for (LikeComment like : likes) {
            if(like.checkUser(user)){
                return like;
            }
        }
        return null;
    }

    public void addChild(Comment comment){
        children.add(comment);
    }
}

