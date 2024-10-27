package com.example.first.framework.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.first.framework.auth.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
        Optional<User> findByUsername(String username);
}
