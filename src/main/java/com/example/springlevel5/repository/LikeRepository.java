package com.example.springlevel5.repository;

import com.example.springlevel5.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface LikeRepository extends JpaRepository<Like, Long> {
    
}
