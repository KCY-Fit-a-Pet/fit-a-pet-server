package com.kcy.fitapet.domain.schedule.dao;

import com.kcy.fitapet.domain.pet.domain.QPet;
import com.kcy.fitapet.domain.pet.domain.QPetSchedule;
import com.kcy.fitapet.domain.schedule.domain.QSchedule;
import com.kcy.fitapet.domain.schedule.domain.Schedule;
import com.kcy.fitapet.domain.schedule.dto.ScheduleInfoDto;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

@Repository
@RequiredArgsConstructor
public class ScheduleQueryRepositoryImpl implements ScheduleQueryRepository {
    private final JPAQueryFactory queryFactory;
    private final QSchedule schedule = QSchedule.schedule;
    private final QPetSchedule petSchedule = QPetSchedule.petSchedule;
    private final QPet pet = QPet.pet;

    @Override
    public List<Schedule> findScheduleByPetIdOnDate(Long petId, LocalDateTime date) {
        return queryFactory.selectFrom(schedule)
                .innerJoin(petSchedule).on(schedule.id.eq(petSchedule.schedule.id))
                .where(petSchedule.pet.id.eq(petId)
                        .and(schedule.reservationDt.between(
                                Expressions.asDateTime(date.withHour(0).withMinute(0).withSecond(0)),
                                Expressions.asDateTime(date.withHour(23).withMinute(59).withSecond(59))
                        )))
                .orderBy(schedule.reservationDt.asc()).fetch();
    }

    @Override
    public List<Schedule> findTopCountSchedulesByIdOnDate(Long petId, LocalDateTime scheduleDate, Integer count) {
        return queryFactory.selectFrom(schedule)
                .innerJoin(petSchedule).on(schedule.id.eq(petSchedule.schedule.id))
                .where(petSchedule.pet.id.eq(petId)
                        .and(schedule.reservationDt.between(
                                Expressions.asDateTime(scheduleDate),
                                Expressions.asDateTime(scheduleDate.withHour(23).withMinute(59).withSecond(59))
                        )))
                .orderBy(schedule.reservationDt.asc())
                .limit(count)
                .fetch();
    }

    @Override
    public List<ScheduleInfoDto.ScheduleInfo> findSchedulesByCalender(LocalDateTime date, List<Long> petIds) {
        return queryFactory
                .select(schedule.id, schedule.scheduleName, schedule.location, schedule.reservationDt, pet.id)
                .from(petSchedule)
                .innerJoin(schedule).on(schedule.id.eq(petSchedule.schedule.id))
                .innerJoin(petSchedule.pet).on(pet.id.in(petIds))
                .where(
                        petSchedule.pet.id.in(petIds)
                                .and(schedule.reservationDt.between(
                                        Expressions.asDateTime(date.withHour(0).withMinute(0).withSecond(0)),
                                        Expressions.asDateTime(date.withHour(23).withMinute(59).withSecond(59))
                                ))
                )
                .orderBy(schedule.reservationDt.asc())
                .transform(
                    groupBy(schedule.id).list(
                        Projections.constructor(
                            ScheduleInfoDto.ScheduleInfo.class,
                            schedule.id,
                            schedule.scheduleName,
                            schedule.location,
                            schedule.reservationDt,
                            list(
                                    Projections.constructor(
                                            ScheduleInfoDto.ParticipantPetInfo.class,
                                            pet.id,
                                            pet.petProfileImg
                                    )
                            )
                        )
                    )
                );
    }
}
