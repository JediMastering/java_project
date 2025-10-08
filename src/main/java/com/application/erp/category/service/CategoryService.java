package com.application.erp.category.service;

import com.application.erp.category.dto.request.CategoryRequest;
import com.application.erp.category.dto.response.CategoryResponse;

import com.application.erp.category.dto.request.CategoryRequest;
import com.application.erp.category.dto.response.CategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {

    Page<CategoryResponse> findCategories(String name, boolean includeInactive, Pageable pageable);

    CategoryResponse findCategoryById(Long id);

    CategoryResponse createCategory(CategoryRequest categoryRequest);

    CategoryResponse updateCategory(Long id, CategoryRequest categoryRequest);

    void deleteCategory(Long id);
}
