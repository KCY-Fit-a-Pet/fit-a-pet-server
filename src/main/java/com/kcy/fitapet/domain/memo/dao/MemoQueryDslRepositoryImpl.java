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

    @Override
    public Slice<MemoInfoDto.MemoInfo> findMemosInMemoCategory(Long memoCategoryId, Pageable pageable, String target) {
         List<MemoInfoDto.MemoInfo> results = queryFactory
                 .select(memoCategory.id, memo.id, memo.title, memo.content, memo.createdAt, memoImage.id, memoImage.imgUrl)
                .from(memo)
                .innerJoin(memoCategory).on(memoCategory.id.eq(memo.memoCategory.id))
                .leftJoin(memoImage).on(memoImage.memo.id.eq(memo.id))
                .where(memoCategory.id.eq(memoCategoryId)
                        .and(QueryDslUtil.matchAgainst(memo.title, memo.content, target))
                )
                .orderBy(QueryDslUtil.getOrderSpecifier(pageable.getSort()).toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .transform(
                        groupBy(memo.id).list(
                                Projections.constructor(
                                        MemoInfoDto.MemoInfo.class,
                                        memo.id,
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
