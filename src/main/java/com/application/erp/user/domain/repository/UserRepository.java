package com.application.erp.user.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.application.erp.user.domain.entity.User;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByPasswordResetToken(String token);
}
