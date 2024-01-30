package com.kcy.fitapet.domain.memo.dao;

import com.kcy.fitapet.domain.memo.domain.QMemo;
import com.kcy.fitapet.domain.memo.domain.QMemoImage;
import com.kcy.fitapet.domain.memo.dto.MemoInfoDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;

import java.util.Optional;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.types.Projections.list;

@Repository
@RequiredArgsConstructor
public class MemoQueryDslRepositoryImpl implements MemoQueryDslRepository {
    private final JPAQueryFactory queryFactory;
    private final QMemo memo = QMemo.memo;
    private final QMemoImage memoImage = QMemoImage.memoImage;


    @Override
    public Optional<MemoInfoDto.MemoInfo> findMemoAndMemoImageUrlsById(Long memoId) {
        return Optional.of(queryFactory
                .select(
                        Projections.constructor(
                                MemoInfoDto.MemoInfo.class,
                                memo.id,
                                memo.title,
                                memo.content,
                                list(memoImage.imgUrl).skipNulls()
                        )
                )
                .from(memo)
                .leftJoin(memoImage).on(memo.id.eq(memoImage.memo.id))
                .where(memo.id.eq(memoId))
                .fetchFirst());
    }
}
