package com.example.springlevel5.repository;

import com.example.springlevel5.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByUser_IdAndName(Long id, String name);
}
