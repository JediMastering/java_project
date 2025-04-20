package com.example.first.framework.auth.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.first.framework.auth.example.model.UserExample;

public interface UserExampleRepository extends JpaRepository<UserExample, Long> {
    boolean existsByEmail(String email);
}