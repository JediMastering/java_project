package com.application.erp.user.domain.repository;

import com.application.erp.user.dto.UserFilter;
import com.application.erp.user.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom {
    Page<User> findUsersByFilter(UserFilter filter, Pageable pageable);
}
