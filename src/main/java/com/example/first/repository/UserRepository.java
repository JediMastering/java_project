package com.example.first.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.first.entity.User;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
