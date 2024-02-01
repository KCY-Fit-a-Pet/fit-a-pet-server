package com.kcy.fitapet.global.common.repository;

import com.querydsl.jpa.impl.JPAQuery;

@FunctionalInterface
public interface QueryHandler {
    JPAQuery<?> apply(JPAQuery<?> query);
}
