package com.example.first.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.first.entity.User;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID>, UserRepositoryCustom {
        Optional<User> findByUsername(String username);
}
