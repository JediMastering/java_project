package com.example.first.repository.impl;

import com.example.first.dto.UserFilter;
import com.example.first.entity.AccessGroup;
import com.example.first.entity.User;
import com.example.first.repository.UserRepositoryCustom;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.*;

import static com.example.first.jooq.tables.TbAccessGroups.TB_ACCESS_GROUPS;
import static com.example.first.jooq.tables.TbUsers.TB_USERS;
import static com.example.first.jooq.tables.TbUsersAccessGroups.TB_USERS_ACCESS_GROUPS;

@Repository
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    private final DSLContext dsl;

    @Autowired
    public UserRepositoryCustomImpl(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public Page<User> findAll(UserFilter filter, Pageable pageable) {
        List<Condition> conditions = createFilterConditions(filter);

        long total = dsl.fetchCount(dsl.selectFrom(TB_USERS).where(conditions));

        List<Long> userIds = fetchCurrentUserPageUserIds(conditions, pageable);

        if (userIds.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), pageable, total);
        }

        Result<Record> userRecordsWithGroups = fetchUsersWithGroups(userIds);

        Map<Long, User> userMap = mapRecordsToUsersWithGroups(userRecordsWithGroups);

        return new PageImpl<>(new ArrayList<>(userMap.values()), pageable, total);
    }

    private List<Condition> createFilterConditions(UserFilter filter) {
        List<Condition> conditions = new ArrayList<>();
        if (filter.getName() != null && !filter.getName().isEmpty()) {
            conditions.add(TB_USERS.USERNAME.likeIgnoreCase("%" + filter.getName() + "%"));
        }
        if (filter.getEmail() != null && !filter.getEmail().isEmpty()) {
            conditions.add(TB_USERS.EMAIL.likeIgnoreCase("%" + filter.getEmail() + "%"));
        }
        return conditions;
    }

    private List<Long> fetchCurrentUserPageUserIds(List<Condition> conditions, Pageable pageable) {
        return dsl.select(TB_USERS.USER_ID)
                .from(TB_USERS)
                .where(conditions)
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch(TB_USERS.USER_ID);
    }

    private Result<Record> fetchUsersWithGroups(List<Long> userIds) {
        return dsl.select(TB_USERS.fields())
                .select(TB_ACCESS_GROUPS.fields())
                .from(TB_USERS)
                .leftJoin(TB_USERS_ACCESS_GROUPS).on(TB_USERS.USER_ID.eq(TB_USERS_ACCESS_GROUPS.USER_ID))
                .leftJoin(TB_ACCESS_GROUPS).on(TB_USERS_ACCESS_GROUPS.ACCESS_GROUP_ID.eq(TB_ACCESS_GROUPS.ACCESS_GROUP_ID))
                .where(TB_USERS.USER_ID.in(userIds))
                .fetch();
    }

    private Map<Long, User> mapRecordsToUsersWithGroups(Result<Record> records) {
        Map<Long, User> userMap = new LinkedHashMap<>();
        for (Record r : records) {
            Long userId = r.get(TB_USERS.USER_ID);
            User user = userMap.computeIfAbsent(userId, id -> {
                User newUser = new User();
                newUser.setUserId(r.get(TB_USERS.USER_ID));
                newUser.setUsername(r.get(TB_USERS.USERNAME));
                newUser.setEmail(r.get(TB_USERS.EMAIL));
                newUser.setPassword(r.get(TB_USERS.PASSWORD));
                return newUser;
            });

            if (r.get(TB_ACCESS_GROUPS.ACCESS_GROUP_ID) != null) {
                AccessGroup group = r.into(TB_ACCESS_GROUPS).into(AccessGroup.class);
                user.getAccessGroups().add(group);
            }
        }
        return userMap;
    }
}