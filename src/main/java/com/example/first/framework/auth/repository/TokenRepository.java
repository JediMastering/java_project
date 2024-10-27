package com.example.first.framework.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.first.framework.auth.entity.Token;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
        Optional<Token> findByAccessTokenAndIsActive(String accessToken, Integer isInteger);
}
