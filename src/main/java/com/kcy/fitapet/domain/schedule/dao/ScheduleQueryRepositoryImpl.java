package com.kcy.fitapet.domain.schedule.dao;

import com.kcy.fitapet.domain.schedule.domain.QSchedule;
import com.kcy.fitapet.domain.schedule.domain.Schedule;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ScheduleQueryRepositoryImpl implements ScheduleQueryRepository {
    private final JPAQueryFactory queryFactory;
    private final QSchedule schedule = QSchedule.schedule;

    @Override
    public List<Schedule> findTopCountScheduleByIdOnDate(Long id, LocalDateTime scheduleDate, Integer count) {
        return queryFactory.selectFrom(schedule)
                .where(schedule.id.eq(id)
                        .and(schedule.reservationDt.between(
                                Expressions.asDateTime(scheduleDate.withHour(0).withMinute(0).withSecond(0)),
                                Expressions.asDateTime(scheduleDate.withHour(23).withMinute(59).withSecond(59))
                        )))
                .orderBy(schedule.reservationDt.asc())
                .limit(count)
                .fetch();
    }
}
