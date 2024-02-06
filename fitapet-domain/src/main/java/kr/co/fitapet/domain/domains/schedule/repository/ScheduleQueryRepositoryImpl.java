package kr.co.fitapet.domain.domains.schedule.repository;


import com.querydsl.core.ResultTransformer;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.fitapet.domain.domains.pet.domain.QPet;
import kr.co.fitapet.domain.domains.pet.domain.QPetSchedule;
import kr.co.fitapet.domain.domains.schedule.domain.QSchedule;
import kr.co.fitapet.domain.domains.schedule.dto.ScheduleInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.types.Projections.list;

@Repository
@RequiredArgsConstructor
public class ScheduleQueryRepositoryImpl implements ScheduleQueryRepository {
    private final JPAQueryFactory queryFactory;
    private final QSchedule schedule = QSchedule.schedule;
    private final QPetSchedule petSchedule = QPetSchedule.petSchedule;
    private final QPet pet = QPet.pet;

    @Override
    public List<Long> findScheduleIds(Long petId, LocalDateTime date) {
        return queryFactory.select(schedule.id)
                .from(petSchedule)
                .innerJoin(schedule).on(schedule.id.eq(petSchedule.schedule.id))
                .where(petSchedule.pet.id.eq(petId)
                        .and(schedule.reservationDt.between(
                                Expressions.asDateTime(date),
                                Expressions.asDateTime(date.withHour(23).withMinute(59).withSecond(59))
                        )))
                .fetch();
    }

    @Override
    public List<Long> findScheduleIds(Long petId, LocalDateTime date, int count) {
        return queryFactory.select(schedule.id)
                .from(petSchedule)
                .innerJoin(schedule).on(schedule.id.eq(petSchedule.schedule.id))
                .where(petSchedule.pet.id.eq(petId)
                        .and(schedule.reservationDt.between(
                                Expressions.asDateTime(date),
                                Expressions.asDateTime(date.withHour(23).withMinute(59).withSecond(59))
                        )))
                .orderBy(schedule.reservationDt.asc())
                .limit(count)
                .fetch();
    }

    @Override
    public List<ScheduleInfoDto.ScheduleInfo> findSchedulesByIds(List<Long> scheduleIds) {
        return queryFactory
                .select(schedule.id, schedule.scheduleName, schedule.location, schedule.reservationDt, pet.id, pet.petProfileImg)
                .from(schedule)
                .innerJoin(petSchedule).on(schedule.id.eq(petSchedule.schedule.id))
                .innerJoin(pet).on(pet.id.eq(petSchedule.pet.id))
                .where(schedule.id.in(scheduleIds))
                .orderBy(schedule.reservationDt.asc())
                .transform(scheduleInfoResultTransformer());
    }

    @Override
    public List<ScheduleInfoDto.ScheduleInfo> findSchedulesByCalender(LocalDateTime date, List<Long> petIds) {
        return queryFactory
                .select(schedule.id, schedule.scheduleName, schedule.location, schedule.reservationDt, pet.id, pet.petProfileImg)
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
                .transform(scheduleInfoResultTransformer());
    }

    private ResultTransformer<List<ScheduleInfoDto.ScheduleInfo>> scheduleInfoResultTransformer() {
        return groupBy(schedule.id).list(
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
        );
    }
}
