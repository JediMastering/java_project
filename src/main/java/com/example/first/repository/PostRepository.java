package com.example.first.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.first.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
