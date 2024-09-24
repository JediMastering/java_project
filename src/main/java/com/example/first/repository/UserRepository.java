package com.example.first.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.first.entity.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
