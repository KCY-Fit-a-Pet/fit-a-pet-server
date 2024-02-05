package kr.co.fitapet.domain.common.util;

import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Slf4j
public class QueryDslUtil {
    private static final Function<Sort.NullHandling, OrderSpecifier.NullHandling> castToQueryDsl = nullHandling -> switch (nullHandling) {
        case NATIVE -> OrderSpecifier.NullHandling.Default;
        case NULLS_FIRST -> OrderSpecifier.NullHandling.NullsFirst;
        case NULLS_LAST -> OrderSpecifier.NullHandling.NullsLast;
    };

    /**
     * match_against 함수를 사용하여 memo 테이블의 title, content 컬럼과 target을 비교한다.
     * @param c1 : memo.title
     * @param c2 : memo.content
     * @param target : 검색어
     */
    public static BooleanExpression matchAgainst(final StringPath c1, final StringPath c2, final String target) {
        if (!StringUtils.hasText(target)) { return null; }
        String template = "'" + target + "*'";
        log.info("template: {}", template);
        return Expressions.booleanTemplate( "function('match_against', {0}, {1}, {2})", c1, c2, template);
    }

    /**
     * LEFT 함수로 문자열을 c2 길이만큼 잘라서 반환한다.
     */
    public static StringExpression left(final StringPath c1, final Expression<Integer> c2) {
        return Expressions.stringTemplate("function('left', {0}, {1})", c1, c2);
    }

    /**
     * Pageable의 sort를 QueryDsl의 OrderSpecifier로 변환한다.
     * @param sort : Pageable의 sort
     */
    public static List<OrderSpecifier<?>> getOrderSpecifier(Sort sort) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();

        for (Sort.Order order : sort) {
            OrderSpecifier.NullHandling nullHandling = castToQueryDsl.apply(order.getNullHandling());
            orders.add(getOrderSpecifier(order, nullHandling));
        }

        return orders;
    }

    private static OrderSpecifier<?> getOrderSpecifier(Sort.Order order, OrderSpecifier.NullHandling nullHandling) {
        Order orderBy = order.isAscending() ? Order.ASC : Order.DESC;
        log.info("isAscending: {}", order.isAscending());

        return createOrderSpecifier(orderBy, Expressions.stringPath(order.getProperty()), nullHandling);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static OrderSpecifier<?> createOrderSpecifier(Order orderBy, Expression<?> expression, OrderSpecifier.NullHandling queryDslNullHandling) {
        if (expression instanceof Operation && ((Operation<?>) expression).getOperator() == Ops.ALIAS) {
            return new OrderSpecifier<>(orderBy, Expressions.stringPath(((Operation<?>) expression).getArg(1).toString()), queryDslNullHandling);
        } else {
            return new OrderSpecifier(orderBy, expression, queryDslNullHandling);
        }
    }
}