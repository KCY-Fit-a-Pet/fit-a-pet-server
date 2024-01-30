package com.kcy.fitapet.global.config;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.boot.model.FunctionContributor;
import org.hibernate.query.sqm.function.SqmFunctionRegistry;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.spi.TypeConfiguration;

/**
 * MySQL의 match_against 함수를 사용하기 위한 FunctionContributor
 *
 * <pre>
 * Boolean Mode 검색 예시
 * {@code
 * public List<Entity> findAllBy(final String entityNameWord, final String entityAttrWord) {
 *    return queryFactory.selectFrom(entity)
 *        .where(
 *             matchAgainst(entity.name, entityNameWord),
 *             matchAgainst(entity.attr, entityAttrWord)
 *        )
 *        .fetch();
 * }
 *
 * private BooleanExpression matchAgainst(final String target, final String searchWord) {
 *    if (!StringUtils.hasText(searchWord)) {
 *       return null
 *    }
 *    return Expressions.booleanTemplate("match({0}) against({1} in boolean mode) > 0", target, searchWord);
 * }
 * </pre>
 *
 * @see <a href="https://velog.io/@ttomy/%EC%82%AC%EC%9A%A9%EC%9E%90-%EC%A0%95%EC%9D%98-dialectmatch-against%EC%82%AC%EC%9A%A9%ED%95%98%EA%B8%B0">참고 블로그</a>
 */
public class MySqlFunctionContributor implements FunctionContributor {
    private static final String FUNCTION_NAME = "match_against";
    private static final String NATURAL_LANGUAGE_PATTERN = "match (?1) against (?2 in natural language mode)";
    private static final String BOOLEAN_PATTERN = "match (?1) against (?2 in boolean mode)";
    private static final String QUERY_EXPANSION_PATTERN = "match (?1) against (?2 in query expansion mode)";

    @Override
    public void contributeFunctions(FunctionContributions functionContributions) {
        SqmFunctionRegistry registry = functionContributions.getFunctionRegistry();
        TypeConfiguration typeConfiguration = functionContributions.getTypeConfiguration();

        registry.registerPattern( FUNCTION_NAME, NATURAL_LANGUAGE_PATTERN, typeConfiguration.getBasicTypeRegistry().resolve(StandardBasicTypes.DOUBLE) );
        registry.registerPattern( FUNCTION_NAME, BOOLEAN_PATTERN, typeConfiguration.getBasicTypeRegistry().resolve(StandardBasicTypes.DOUBLE) );
        registry.registerPattern( FUNCTION_NAME, QUERY_EXPANSION_PATTERN, typeConfiguration.getBasicTypeRegistry().resolve(StandardBasicTypes.DOUBLE) );
    }
}
