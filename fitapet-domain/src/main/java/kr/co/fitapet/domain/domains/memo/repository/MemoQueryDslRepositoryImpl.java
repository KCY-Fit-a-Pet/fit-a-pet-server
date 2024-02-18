package kr.co.fitapet.domain.domains.memo.repository;

import com.querydsl.core.ResultTransformer;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.fitapet.domain.common.util.QueryDslUtil;
import kr.co.fitapet.domain.common.util.SliceUtil;
import kr.co.fitapet.domain.domains.memo.domain.QMemo;
import kr.co.fitapet.domain.domains.memo.domain.QMemoCategory;
import kr.co.fitapet.domain.domains.memo.domain.QMemoImage;
import kr.co.fitapet.domain.domains.memo.dto.MemoInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.List;
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
        List<MemoInfoDto.MemoInfo> result = queryFactory
                .from(memo)
                .leftJoin(memoCategory).on(memoCategory.id.eq(memo.memoCategory.id))
                .leftJoin(memoImage).on(memo.id.eq(memoImage.memo.id))
                .where(memo.id.eq(memoId))
                .transform(
                        groupBy(memoCategory.id, memo.id).list(
                                Projections.constructor(
                                        MemoInfoDto.MemoInfo.class,
                                        memo.id,
                                        memoCategory.categoryName,
                                        memo.title,
                                        memo.content,
                                        memo.createdAt,
                                        GroupBy.list(
                                                Projections.constructor(
                                                        MemoInfoDto.MemoImageInfo.class,
                                                        memoImage.id,
                                                        memoImage.imgUrl
                                                ).skipNulls()
                                        )
                                )
                        )
                );

        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
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
     * 		LEFT JOIN memo m ON m.category_id = c.id
     * 		WHERE c.id = ?
     * 		AND MATCH(m.title, m.content) AGAINST('병원*' IN BOOLEAN MODE)
     * 	    ORDER BY m.created_at DESC
     * 		LIMIT ?, ?
     *     ) id
     * )
     * ORDER BY m.created_at DESC
     * ;
     * </pre>
     */
    @Override
    public Slice<MemoInfoDto.MemoSummaryInfo> findMemosInMemoCategory(Long memoCategoryId, Pageable pageable, String target) {
         List<MemoInfoDto.MemoSummaryInfo> results = queryFactory
                .from(memo)
                .leftJoin(memoImage).on(memoImage.memo.id.eq(memo.id))
                .where(memo.id.in(
                        queryFactory
                                .select(memo.id)
                                .from(memoCategory)
                                .leftJoin(memo).on(memo.memoCategory.id.eq(memoCategory.id))
                                .where(memoCategory.id.eq(memoCategoryId)
                                        .and(QueryDslUtil.matchAgainstTwoElemBooleanMode(memo.title, memo.content, target))
                                )
                                .orderBy(QueryDslUtil.getOrderSpecifier(pageable.getSort()).toArray(OrderSpecifier[]::new))
                                .offset(pageable.getOffset())
                                .limit(pageable.getPageSize() + 1)
                ))
                 .orderBy(QueryDslUtil.getOrderSpecifier(pageable.getSort()).toArray(OrderSpecifier[]::new))
                .transform(createMemoInfoDtoResultTransformer());

         return SliceUtil.toSlice(results, pageable);
    }

    @Override
    public Slice<MemoInfoDto.MemoSummaryInfo> findMemosByPetId(Long petId, Pageable pageable) {
        List<MemoInfoDto.MemoSummaryInfo> results = queryFactory
                .from(memoCategory)
                .leftJoin(memo).on(memo.memoCategory.id.eq(memoCategory.id))
                .leftJoin(memoImage).on(memoImage.memo.id.eq(memo.id))
                .where(memo.id.in(
                        queryFactory
                                .select(memo.id)
                                .from(memoCategory)
                                .leftJoin(memo).on(memo.memoCategory.id.eq(memoCategory.id))
                                .where(memoCategory.pet.id.eq(petId))
                                .orderBy(QueryDslUtil.getOrderSpecifier(pageable.getSort()).toArray(OrderSpecifier[]::new))
                                .offset(pageable.getOffset())
                                .limit(pageable.getPageSize() + 1)
                ))
                .orderBy(QueryDslUtil.getOrderSpecifier(pageable.getSort()).toArray(OrderSpecifier[]::new))
                .transform(createMemoInfoDtoResultTransformer());

        return SliceUtil.toSlice(results, pageable);
    }

    private ResultTransformer<List<MemoInfoDto.MemoSummaryInfo>> createMemoInfoDtoResultTransformer() {
        return groupBy(memo.id).list(
                Projections.constructor(
                        MemoInfoDto.MemoSummaryInfo.class,
                        memo.id,
                        queryFactory.select(memoCategory.categoryName).from(memoCategory).where(memoCategory.id.eq(memo.memoCategory.id)),
                        QueryDslUtil.left(memo.title, Expressions.constant(19)),
                        QueryDslUtil.left(memo.content, Expressions.constant(16)),
                        memo.createdAt,
                        GroupBy.list(
                                Projections.constructor(
                                        MemoInfoDto.MemoImageInfo.class,
                                        memoImage.id,
                                        memoImage.imgUrl
                                )
                        ).getExpression()
                )
        );
    }
}
