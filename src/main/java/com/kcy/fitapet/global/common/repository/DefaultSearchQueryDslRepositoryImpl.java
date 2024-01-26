package com.kcy.fitapet.global.common.repository;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.querydsl.SimpleEntityPathResolver;

import java.util.List;
import java.util.Map;

public class DefaultSearchQueryDslRepositoryImpl<T> implements DefaultSearchQueryDslRepository<T> {
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;
    private final EntityPath<T> path;

    public DefaultSearchQueryDslRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        this.em = entityManager;
        this.queryFactory = new JPAQueryFactory(entityManager);
        this.path = SimpleEntityPathResolver.INSTANCE.createPath(entityInformation.getJavaType());
    }

    public DefaultSearchQueryDslRepositoryImpl(Class<T> type, EntityManager entityManager) {
        this.em = entityManager;
        this.queryFactory = new JPAQueryFactory(entityManager);
        this.path = new EntityPathBase<>(type, "entity");
    }

    @Override
    public List<T> findList(Predicate predicate, QueryHandler queryHandler, Sort sort) {
        return null;
    }

    @Override
    public Page<T> findPage(Predicate predicate, QueryHandler queryHandler, Pageable pageable) {
        return null;
    }

    @Override
    public <P> List<P> selectList(Predicate predicate, Class<P> type, Map<String, Expression<?>> bindings, QueryHandler queryHandler, Sort sort) {
        return null;
    }

    @Override
    public <P> Page<P> selectPage(Predicate predicate, Class<P> type, Map<String, Expression<?>> bindings, QueryHandler queryHandler, Pageable pageable) {
        return null;
    }
}
