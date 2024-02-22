package kr.co.fitapet.domain.domains.care_log.repository;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.fitapet.domain.domains.care_log.domain.CareLog;
import kr.co.fitapet.domain.domains.care_log.domain.QCareLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CareLogQueryRepositoryImpl implements CareLogQueryRepository {
    private final JPAQueryFactory queryFactory;
    private final QCareLog careLog = QCareLog.careLog;

    @Override
    public boolean existsByCareDateIdAndLogDate(Long careDateId, LocalDateTime logDate) {
        return queryFactory.selectFrom(careLog)
                .where(careLog.careDate.id.eq(careDateId)
                        .and(careLog.logDate.between(
                            Expressions.asDateTime(logDate.withHour(0).withMinute(0).withSecond(0)),
                            Expressions.asDateTime(logDate.withHour(23).withMinute(59).withSecond(59))
                        ))
                        )
                .fetchFirst() != null;
    }

    @Override
    public Optional<CareLog> findByCareDateIdAndLogDate(Long careDateId, LocalDateTime logDate) {
        return Optional.ofNullable(queryFactory.selectFrom(careLog)
                .where(careLog.careDate.id.eq(careDateId)
                        .and(careLog.logDate.between(
                            Expressions.asDateTime(logDate.withHour(0).withMinute(0).withSecond(0)),
                            Expressions.asDateTime(logDate.withHour(23).withMinute(59).withSecond(59))
                        ))
                        )
                .fetchFirst());
    }

}
