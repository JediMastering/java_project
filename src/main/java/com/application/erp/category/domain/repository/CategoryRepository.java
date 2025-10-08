package com.application.erp.category.domain.repository;

import com.application.erp.category.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>, CategoryRepositoryCustom {

    List<Category> findAllByUser_UserIdAndActive(Long userId, boolean active);

    List<Category> findAllByUser_UserId(Long userId);
}
