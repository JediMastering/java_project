package com.example.first.repository.impl;

import com.example.first.dto.UserFilter;
import com.example.first.entity.User;
import com.example.first.repository.UserRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.example.first.entity.QUser;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    private JPAQueryFactory queryFactory;

    @PostConstruct
    public void init() {
        queryFactory = new JPAQueryFactory(entityManager);
    }

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

        List<User> users = queryFactory.selectFrom(user)
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.selectFrom(user).where(predicate).fetchCount();

        return new PageImpl<>(users, pageable, total);
    }
}