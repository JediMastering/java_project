package com.application.erp.category.domain.repository.impl;

import com.application.erp.category.domain.entity.Category;
import com.application.erp.category.domain.repository.CategoryRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class CategoryRepositoryCustomImpl implements CategoryRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<Category> findCategories(Long userId, String name, boolean includeInactive, Pageable pageable) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Category> cq = cb.createQuery(Category.class);
        Root<Category> category = cq.from(Category.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(category.get("user").get("userId"), userId));

        if (StringUtils.hasText(name)) {
            predicates.add(cb.like(cb.lower(category.get("name")), "%" + name.toLowerCase() + "%"));
        }

        if (!includeInactive) {
            predicates.add(cb.isTrue(category.get("active")));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        // Order by name ASC
        cq.orderBy(cb.asc(category.get("name")));

        TypedQuery<Category> query = em.createQuery(cq);

        // Count query
        long total = countCategories(userId, name, includeInactive);

        // Apply pagination
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<Category> categories = query.getResultList();

        return new PageImpl<>(categories, pageable, total);
    }

    private long countCategories(Long userId, String name, boolean includeInactive) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Category> category = cq.from(Category.class);
        cq.select(cb.count(category));

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(category.get("user").get("userId"), userId));

        if (StringUtils.hasText(name)) {
            predicates.add(cb.like(cb.lower(category.get("name")), "%" + name.toLowerCase() + "%"));
        }

        if (!includeInactive) {
            predicates.add(cb.isTrue(category.get("active")));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        return em.createQuery(cq).getSingleResult();
    }
}