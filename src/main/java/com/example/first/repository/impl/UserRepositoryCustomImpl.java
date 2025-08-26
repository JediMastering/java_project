package com.example.first.repository.impl;

import com.example.first.dto.UserFilter;
import com.example.first.entity.QUser;
import com.example.first.entity.User;
import com.example.first.repository.UserRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<User> findAll(UserFilter filter, Pageable pageable) {
        QUser user = QUser.user;
        BooleanBuilder predicate = new BooleanBuilder();

        if (filter.getName() != null && !filter.getName().isEmpty()) {
            predicate.and(user.username.containsIgnoreCase(filter.getName()));
        }

        if (filter.getEmail() != null && !filter.getEmail().isEmpty()) {
            predicate.and(user.email.containsIgnoreCase(filter.getEmail()));
        }

        if (filter.getRole() != null && !filter.getRole().isEmpty()) {
            predicate.and(user.roles.any().name.containsIgnoreCase(filter.getRole()));
        }

        JPAQuery<User> query = new JPAQuery<>(entityManager);
        List<User> users = query.from(user)
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = query.fetchCount();

        return new PageImpl<>(users, pageable, total);
    }
}
