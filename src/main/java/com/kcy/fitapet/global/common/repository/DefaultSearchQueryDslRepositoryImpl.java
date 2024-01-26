package com.kcy.fitapet.global.common.repository;

import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
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
        Assert.notNull(pageable, "pageable must not be null!");


    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private JPAQuery<?> buildWithoutSelect(Predicate predicate, Map<String, Expression<?>> bindings, QueryHandler queryHandler, Sort sort) {
        JPAQuery<?> query = queryFactory.from(path);

        if (predicate != null) query = query.where(predicate);
        if (queryHandler != null) query = queryHandler.apply(query);
        if (sort != null) {
            if (sort instanceof QSort qSort) {
                query = query.orderBy(qSort.getOrderSpecifiers().toArray(new OrderSpecifier[0]));
            } else {
                Function<Sort.NullHandling, OrderSpecifier.NullHandling> castToQueryDsl = nullHandling -> switch (nullHandling) {
                    case NATIVE -> OrderSpecifier.NullHandling.Default;
                    case NULLS_FIRST -> OrderSpecifier.NullHandling.NullsFirst;
                    case NULLS_LAST -> OrderSpecifier.NullHandling.NullsLast;
                };

                for (Sort.Order order : sort) {
                    OrderSpecifier.NullHandling queryDslNullHandling = castToQueryDsl.apply(order.getNullHandling());

                    Order orderBy = order.isAscending() ? Order.ASC : Order.DESC;
                    OrderSpecifier<?> os;

                    if (bindings != null && bindings.containsKey(order.getProperty())) {
                        Expression<?> expression = bindings.get(order.getProperty());

                        if (expression instanceof Operation && ((Operation<?>) expression).getOperator() == Ops.ALIAS) {
                            os = new OrderSpecifier<>(orderBy, Expressions.stringPath(((Operation<?>) expression).getArg(1).toString()), queryDslNullHandling);
                        } else {
                            os = new OrderSpecifier(orderBy, expression, queryDslNullHandling);
                        }
                    } else {
                        os = new OrderSpecifier<>(orderBy, Expressions.stringPath(order.getProperty()), queryDslNullHandling);
                    }

                    query = query.orderBy(os);
                }
            }
        }

        return query;
    }
}
