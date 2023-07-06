package com.example.springlevel5.entity;

import com.example.springlevel5.dto.PostRequestDto;
import com.example.springlevel5.repository.PostRepository;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "posts")
public class Post extends Timestamped {
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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    @OrderBy("id desc")
    private List<Comment> comments;

    @Column(name = "like_count", nullable = false)
    @ColumnDefault("0")
    private Integer likeCount;

    public Post(PostRequestDto requestDto, User user) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.user = user;
        this.username = user.getUsername();
    }

    public void update(PostRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
    }

    public void updateLike(boolean like) {
        if (like) {
            this.likeCount++;
        } else {
            this.likeCount--;
        }
    }
}


