package com.example.first.repository;

import com.example.first.entity.AccessGroupPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessGroupPermissionRepository extends JpaRepository<AccessGroupPermission, Long> {
}
