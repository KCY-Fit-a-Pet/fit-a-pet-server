package com.kcy.fitapet.domain.log.dao;

import com.kcy.fitapet.domain.log.domain.QCareLog;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

}
