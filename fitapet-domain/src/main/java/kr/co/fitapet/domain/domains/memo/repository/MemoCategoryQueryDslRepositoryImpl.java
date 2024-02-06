package kr.co.fitapet.domain.domains.memo.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.fitapet.domain.domains.memo.domain.QMemo;
import kr.co.fitapet.domain.domains.memo.domain.QMemoCategory;
import kr.co.fitapet.domain.domains.memo.dto.MemoCategoryInfoDto;
import kr.co.fitapet.domain.domains.pet.domain.QPet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemoCategoryQueryDslRepositoryImpl implements MemoCategoryQueryDslRepository {
    private final JPAQueryFactory queryFactory;
    private final QMemoCategory memoCategory = QMemoCategory.memoCategory;
    private final QMemo memo = QMemo.memo;
    private final QPet pet = QPet.pet;

    @Override
    public List<Long> findRootMemoCategoryIdByPetId(List<Long> petIds) {
        return queryFactory.select(memoCategory.id)
                .from(memoCategory)
                .leftJoin(pet).on(pet.id.eq(memoCategory.pet.id))
                .where(pet.id.in(petIds).and(memoCategory.parent.id.isNull()))
                .fetch();
    }

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
