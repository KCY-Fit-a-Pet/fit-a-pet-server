package com.kcy.fitapet.global.common.repository;

import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.querydsl.QSort;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

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
        return this.buildWithoutSelect(predicate, null, queryHandler, sort).select(path).fetch();
    }

    @Override
    public Page<T> findPage(Predicate predicate, QueryHandler queryHandler, Pageable pageable) {
        Assert.notNull(pageable, "pageable must not be null!");

        JPAQuery<?> query = this.buildWithoutSelect(predicate, null, queryHandler, pageable.getSort()).select(path);

        int totalSize = query.fetch().size();
        query = query.offset(pageable.getOffset()).limit(pageable.getPageSize());

        return new PageImpl<>(query.select(path).fetch(), pageable, totalSize);
    }

    @Override
    public <P> List<P> selectList(Predicate predicate, Class<P> type, Map<String, Expression<?>> bindings, QueryHandler queryHandler, Sort sort) {
        return this.buildWithoutSelect(predicate, bindings, queryHandler, sort).select(Projections.bean(type, bindings)).fetch();
    }

    @Override
    public <P> Page<P> selectPage(Predicate predicate, Class<P> type, Map<String, Expression<?>> bindings, QueryHandler queryHandler, Pageable pageable) {
        Assert.notNull(pageable, "pageable must not be null!");

        JPAQuery<?> query = this.buildWithoutSelect(predicate, bindings, queryHandler, pageable.getSort()).select(path);

        int totalSize = query.fetch().size();
        query = query.offset(pageable.getOffset()).limit(pageable.getPageSize());

        return new PageImpl<>(query.select(Projections.bean(type, bindings)).fetch(), pageable, totalSize);
    }

    /**
     * 파라미터를 기반으로 Querydsl의 JPAQuery를 생성합니다.
     */
    private JPAQuery<?> buildWithoutSelect(Predicate predicate, Map<String, Expression<?>> bindings, QueryHandler queryHandler, Sort sort) {
        JPAQuery<?> query = queryFactory.from(path);

        applyPredicate(predicate, query);
        applyQueryHandler(queryHandler, query);
        applySort(query, sort, bindings);

        return query;
    }

    /**
     *
     */
    private void applyPredicate(Predicate predicate, JPAQuery<?> query) {
        if (predicate != null) query.where(predicate);
    }

    private void applyQueryHandler(QueryHandler queryHandler, JPAQuery<?> query) {
        if (queryHandler != null) queryHandler.apply(query);
    }

    private void applySort(JPAQuery<?> query, Sort sort, Map<String, Expression<?>> bindings) {
        if (sort != null) {
            if (sort instanceof QSort qSort) {
                query.orderBy(qSort.getOrderSpecifiers().toArray(new OrderSpecifier[0]));
            } else {
                applySortOrders(query, sort, bindings);
            }
        }
    }

    private void applySortOrders(JPAQuery<?> query, Sort sort, Map<String, Expression<?>> bindings) {
        for (Sort.Order order : sort) {
            OrderSpecifier.NullHandling queryDslNullHandling = getQueryDslNullHandling(order);

            OrderSpecifier<?> os = getOrderSpecifier(order, bindings, queryDslNullHandling);

            query.orderBy(os);
        }
    }

    private OrderSpecifier.NullHandling getQueryDslNullHandling(Sort.Order order) {
        Function<Sort.NullHandling, OrderSpecifier.NullHandling> castToQueryDsl = nullHandling -> switch (nullHandling) {
            case NATIVE -> OrderSpecifier.NullHandling.Default;
            case NULLS_FIRST -> OrderSpecifier.NullHandling.NullsFirst;
            case NULLS_LAST -> OrderSpecifier.NullHandling.NullsLast;
        };

        return castToQueryDsl.apply(order.getNullHandling());
    }

    private OrderSpecifier<?> getOrderSpecifier(Sort.Order order, Map<String, Expression<?>> bindings, OrderSpecifier.NullHandling queryDslNullHandling) {
        Order orderBy = order.isAscending() ? Order.ASC : Order.DESC;

        if (bindings != null && bindings.containsKey(order.getProperty())) {
            Expression<?> expression = bindings.get(order.getProperty());
            return createOrderSpecifier(orderBy, expression, queryDslNullHandling);
        } else {
            return createOrderSpecifier(orderBy, Expressions.stringPath(order.getProperty()), queryDslNullHandling);
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private OrderSpecifier<?> createOrderSpecifier(Order orderBy, Expression<?> expression, OrderSpecifier.NullHandling queryDslNullHandling) {
        if (expression instanceof Operation && ((Operation<?>) expression).getOperator() == Ops.ALIAS) {
            return new OrderSpecifier<>(orderBy, Expressions.stringPath(((Operation<?>) expression).getArg(1).toString()), queryDslNullHandling);
        } else {
            return new OrderSpecifier(orderBy, expression, queryDslNullHandling);
        }
    }
}
