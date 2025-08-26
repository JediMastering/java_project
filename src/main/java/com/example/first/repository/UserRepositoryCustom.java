package com.example.first.repository;

import com.example.first.dto.UserFilter;
import com.example.first.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom {
    Page<User> findAll(UserFilter filter, Pageable pageable);
}
