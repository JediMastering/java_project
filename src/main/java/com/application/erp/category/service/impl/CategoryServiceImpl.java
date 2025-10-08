package com.application.erp.category.service.impl;

import com.application.erp.category.domain.entity.Category;
import com.application.erp.category.domain.repository.CategoryRepository;
import com.application.erp.category.dto.request.CategoryRequest;
import com.application.erp.category.dto.response.CategoryResponse;
import com.application.erp.category.service.CategoryService;
import com.application.erp.shared.exception.EntityNotFoundException;
import com.application.erp.user.domain.entity.User;
import com.application.erp.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    public Page<CategoryResponse> findCategories(String name, boolean includeInactive, Pageable pageable) {
        User user = getCurrentUser();
        Page<Category> categoryPage = categoryRepository.findCategories(user.getUserId(), name, includeInactive, pageable);
        return categoryPage.map(this::toCategoryResponse);
    }

    @Override
    public CategoryResponse findCategoryById(Long id) {
        User user = getCurrentUser();
        Category category = categoryRepository.findById(id)
                .filter(c -> c.getUser().getUserId().equals(user.getUserId()))
                .orElseThrow(() -> new EntityNotFoundException(id, Category.class));
        return toCategoryResponse(category);
    }

    @Override
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        User user = getCurrentUser();
        Category category = new Category();
        BeanUtils.copyProperties(categoryRequest, category);
        category.setUser(user);
        Category savedCategory = categoryRepository.save(category);
        return toCategoryResponse(savedCategory);
    }

    @Override
    public CategoryResponse updateCategory(Long id, CategoryRequest categoryRequest) {
        User user = getCurrentUser();
        Category category = categoryRepository.findById(id)
                .filter(c -> c.getUser().getUserId().equals(user.getUserId()))
                .orElseThrow(() -> new EntityNotFoundException(id, Category.class));
        BeanUtils.copyProperties(categoryRequest, category);
        Category updatedCategory = categoryRepository.save(category);
        return toCategoryResponse(updatedCategory);
    }

    @Override
    public void deleteCategory(Long id) {
        User user = getCurrentUser();
        Category category = categoryRepository.findById(id)
                .filter(c -> c.getUser().getUserId().equals(user.getUserId()))
                .orElseThrow(() -> new EntityNotFoundException(id, Category.class));
        category.setActive(false);
        categoryRepository.save(category);
    }

    private User getCurrentUser() {
        String userIdStr = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId = Long.parseLong(userIdStr);
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
    }

    private CategoryResponse toCategoryResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .active(category.isActive())
                .build();
    }
}
