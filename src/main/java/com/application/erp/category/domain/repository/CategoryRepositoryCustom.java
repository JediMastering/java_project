package com.application.erp.category.domain.repository;

import com.application.erp.category.domain.entity.Category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryRepositoryCustom {

    Page<Category> findCategories(Long userId, String name, boolean includeInactive, Pageable pageable);
}
