package com.kcy.fitapet.global.common.repository;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;

/**
 * QueryDSL을 이용한 검색 조건을 처리하는 기본적인 메서드를 선언한 인터페이스
 * @author 양재서
 */
public interface DefaultSearchQueryDslRepository<T> {
    /**
     * 검색 조건에 해당하는 도메인(혹은 DTO) 리스트를 조회하는 메서드
     * @param predicate : 검색 조건
     * @param queryHandler : 검색 조건에 추가적으로 적용할 조건
     * @param sort : 정렬 조건
     *
     * <pre>
     * {@code
     *
     * }
     * </pre>
     */
    List<T> findList(Predicate predicate, QueryHandler queryHandler, Sort sort);

    /**
     * 검색 조건에 해당하는 도메인(혹은 DTO) 페이지를 조회하는 메서드
     * @param predicate :
     * @param queryHandler :
     * @param pageable :
     */
    Page<T> findPage(Predicate predicate, QueryHandler queryHandler, Pageable pageable);

    /**
     * 검색 조건에 해당하는 도메인(혹은 DTO) 리스트를 조회하는 메서드
     * @param predicate : 검색 조건
     * @param type : 조회할 도메인(혹은 DTO) 타입
     * @param bindings :
     * @param queryHandler : 검색 조건에 추가적으로 적용할 조건
     * @param sort : 정렬 조건
     */
    <P> List<P> selectList(Predicate predicate, Class<P> type, Map<String, Expression<?>> bindings, QueryHandler queryHandler, Sort sort);

    /**
     * 검색 조건에 해당하는 도메인(혹은 DTO) 페이지를 조회하는 메서드
     * @param predicate :
     * @param type :
     * @param bindings :
     * @param queryHandler :
     * @param pageable :
     */
    <P> Page<P> selectPage(Predicate predicate, Class<P> type, Map<String, Expression<?>> bindings, QueryHandler queryHandler, Pageable pageable);
}
