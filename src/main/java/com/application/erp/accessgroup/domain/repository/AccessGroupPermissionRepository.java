package com.application.erp.accessgroup.domain.repository;

import com.application.erp.accessgroup.domain.entity.AccessGroupPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessGroupPermissionRepository extends JpaRepository<AccessGroupPermission, Long> {
}
