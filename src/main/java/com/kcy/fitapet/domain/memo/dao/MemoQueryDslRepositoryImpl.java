package com.kcy.fitapet.domain.memo.dao;

import com.kcy.fitapet.domain.memo.domain.QMemo;
import com.kcy.fitapet.domain.memo.domain.QMemoCategory;
import com.kcy.fitapet.domain.memo.domain.QMemoImage;
import com.kcy.fitapet.domain.memo.dto.MemoInfoDto;
import com.kcy.fitapet.global.common.util.querydsl.QueryDslUtil;
import com.kcy.fitapet.global.common.util.querydsl.RepositorySliceHelper;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.types.Projections.list;
import static com.querydsl.core.types.dsl.Expressions.constant;
import static com.querydsl.core.types.dsl.Expressions.constantAs;

@Repository
@RequiredArgsConstructor
@Slf4j
public class MemoQueryDslRepositoryImpl implements MemoQueryDslRepository {
    private final JPAQueryFactory queryFactory;
    private final QMemo memo = QMemo.memo;
    private final QMemoImage memoImage = QMemoImage.memoImage;
    private final QMemoCategory memoCategory = QMemoCategory.memoCategory;

    @Override
    public Optional<MemoInfoDto.MemoInfo> findMemoAndMemoImageUrlsById(Long memoId) {
        return Optional.of(queryFactory
                .select(
                        Projections.constructor(
                                MemoInfoDto.MemoInfo.class,
                                memo.id,
                                memoCategory.categoryName,
                                QueryDslUtil.left(memo.title, Expressions.constant(19)),
                                QueryDslUtil.left(memo.content, Expressions.constant(16)),
                                memo.createdAt,
                                list(
                                        Projections.constructor(
                                                MemoInfoDto.MemoImageInfo.class,
                                                memoImage.id,
                                                memoImage.imgUrl
                                        ).skipNulls()
                                ).skipNulls()
                        )
                )
                .from(memo)
                .leftJoin(memoImage).on(memo.id.eq(memoImage.memo.id))
                .where(memo.id.eq(memoId))
                .fetchFirst());
    }

    /**
     * 메모 카테고리 ID로 메모 리스트 조회 및 검색
     * <pre>
     * SELECT (SELECT category_name FROM memo_category WHERE id = 4) category_name, m.id, LEFT(m.title, 19), LEFT(m.content, 16) content, m.created_at, i.id, i.img_url
     * FROM memo m
     * LEFT JOIN memo_image i ON i.memo_id = m.id
     * WHERE m.id IN (
     * 	SELECT *
     *     FROM (
     * 		SELECT m.id
     * 		FROM memo_category c
     * 		INNER JOIN memo m ON m.category_id = c.id
     * 		WHERE c.id = 4
     * 		AND MATCH(m.title, m.content) AGAINST('병원*' IN BOOLEAN MODE)
     * 		LIMIT 4
     *     ) id
     * )
     * ORDER BY m.created_at DESC
     * ;
     * </pre>
     */
    @Override
    public Slice<MemoInfoDto.MemoInfo> findMemosInMemoCategory(Long memoCategoryId, Pageable pageable, String target) {
         List<MemoInfoDto.MemoInfo> results = queryFactory
                .from(memo)
                .leftJoin(memoImage).on(memoImage.memo.id.eq(memo.id))
                .where(memo.id.in(
                        queryFactory
                                .select(memo.id)
                                .from(memoCategory)
                                .leftJoin(memo).on(memo.memoCategory.id.eq(memoCategory.id))
                                .where(memoCategory.id.eq(memoCategoryId)
                                        .and(QueryDslUtil.matchAgainst(memo.title, memo.content, target))
                                )
                                .offset(pageable.getOffset())
                                .limit(pageable.getPageSize() + 1)
                ))
                .orderBy(QueryDslUtil.getOrderSpecifier(pageable.getSort()).toArray(OrderSpecifier[]::new))
                .transform(
                        groupBy(memo.id).list(
                                Projections.constructor(
                                        MemoInfoDto.MemoInfo.class,
                                        memo.id,
                                        queryFactory.select(memoCategory.categoryName).from(memoCategory).where(memoCategory.id.eq(memoCategoryId)),
                                        QueryDslUtil.left(memo.title, Expressions.constant(19)),
                                        QueryDslUtil.left(memo.content, Expressions.constant(16)),
                                        memo.createdAt,
                                        list(
                                                Projections.constructor(
                                                        MemoInfoDto.MemoImageInfo.class,
                                                        memoImage.id,
                                                        memoImage.imgUrl
                                                ).skipNulls()
                                        ).skipNulls()
                                )
                        )
                );

         return RepositorySliceHelper.toSlice(results, pageable);
    }
}
