package com.example.first.framework.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.first.framework.auth.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
