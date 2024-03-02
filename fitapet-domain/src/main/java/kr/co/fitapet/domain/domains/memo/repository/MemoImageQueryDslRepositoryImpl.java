package kr.co.fitapet.domain.domains.memo.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.fitapet.domain.domains.memo.domain.QMemo;
import kr.co.fitapet.domain.domains.memo.domain.QMemoImage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class MemoImageQueryDslRepositoryImpl implements MemoImageQueryDslRepository {
    private final JPAQueryFactory queryFactory;
    private final QMemo memo = QMemo.memo;
    private final QMemoImage memoImage = QMemoImage.memoImage;

    @Override
    public List<String> findMemoImageUrlsByMemoIds(List<Long> memoIds) {
        return queryFactory
                .select(memoImage.imgUrl)
                .from(memoImage)
                .where(memoImage.memo.id.in(memoIds))
                .fetch();
    }
}
