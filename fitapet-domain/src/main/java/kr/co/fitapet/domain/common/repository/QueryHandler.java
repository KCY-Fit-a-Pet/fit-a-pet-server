package kr.co.fitapet.domain.common.repository;

import com.querydsl.jpa.impl.JPAQuery;

@FunctionalInterface
public interface QueryHandler {
    JPAQuery<?> apply(JPAQuery<?> query);
}
