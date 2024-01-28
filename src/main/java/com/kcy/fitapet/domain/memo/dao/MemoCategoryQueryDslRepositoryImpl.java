package com.kcy.fitapet.domain.memo.dao;

import com.kcy.fitapet.domain.memo.domain.QMemo;
import com.kcy.fitapet.domain.memo.domain.QMemoCategory;
import com.kcy.fitapet.domain.memo.dto.MemoCategoryInfoDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemoCategoryQueryDslRepositoryImpl implements MemoCategoryQueryDslRepository {
    private final JPAQueryFactory queryFactory;
    private final QMemoCategory memoCategory = QMemoCategory.memoCategory;
    private final QMemo memo = QMemo.memo;

    @Override
    public MemoCategoryInfoDto.MemoCategoryQueryDslRes findMemoCategoryById(Long memoCategoryId) {
        return queryFactory.select(
                Projections.constructor(
                        MemoCategoryInfoDto.MemoCategoryQueryDslRes.class,
                        memoCategory.id,
                        memoCategory.categoryName,
                        memoCategory.parent.id,
                        memoCategory.id.count()
                ))
                .from(memoCategory)
                .leftJoin(memo).on(memo.memoCategory.id.eq(memoCategory.id))
                .where(memoCategory.id.eq(memoCategoryId))
                .groupBy(memoCategory.id, memoCategory.categoryName)
                .fetchFirst();
    }

    @Override
    public List<MemoCategoryInfoDto.MemoCategoryQueryDslRes> findMemoCategoriesByParent(Long parentId) {
        return queryFactory.select(
                Projections.constructor(
                        MemoCategoryInfoDto.MemoCategoryQueryDslRes.class,
                        memoCategory.id,
                        memoCategory.categoryName,
                        memoCategory.parent.id,
                        memoCategory.id.count()
                ))
                .from(memoCategory)
                .leftJoin(memo).on(memo.memoCategory.id.eq(memoCategory.id))
                .where(memoCategory.parent.id.eq(parentId))
                .groupBy(memoCategory.id, memoCategory.categoryName)
                .fetch();
    }
}
