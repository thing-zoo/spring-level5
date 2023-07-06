package com.example.springlevel5.repository;

import com.example.springlevel5.entity.Comment;
import com.example.springlevel5.entity.Like;
import com.example.springlevel5.entity.Post;
import com.example.springlevel5.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndPost(User user, Post post);

    Optional<Like> findByUserAndComment(User user, Comment comment);
}
