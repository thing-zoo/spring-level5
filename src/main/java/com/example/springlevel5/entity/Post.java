package com.example.springlevel5.entity;

import com.example.springlevel5.dto.PostRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Table(name = "posts")
public class Post extends Timestamped {

    public static QPost qPost = new QPost("post");

    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String username;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy("id desc")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LikePost> likes;

    public Post(PostRequestDto requestDto, User user) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.user = user;
        this.username = user.getUsername();
        this.likes = new ArrayList<>();
    }

    public void update(PostRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
    }

    public void addLike(LikePost likePost){
        this.likes.add(likePost);
    }
    public void DeleteLike(LikePost likePost){
        this.likes.remove(likePost);
    }

    public LikePost changeLike(User user){
        for (LikePost like : likes) {
            if(like.checkUser(user)){
                return like;
            }
        }
        return null;
    }

//    public void updateLikeToPost(User user){
//        this.likes.checkUser(user);
//    }
}


