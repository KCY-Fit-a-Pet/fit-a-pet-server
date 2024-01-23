package com.kcy.fitapet.domain.schedule.dao;

import com.kcy.fitapet.domain.schedule.domain.Schedule;
import com.kcy.fitapet.domain.schedule.dto.ScheduleInfoDto;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleQueryRepository {
    List<Schedule> findScheduleByPetIdOnDate(Long petId, LocalDateTime date);
    List<Schedule> findTopCountSchedulesByIdOnDate(Long id, LocalDateTime scheduleDate, Integer count);

    List<ScheduleInfoDto.ScheduleInfo> findSchedulesByCalender(LocalDateTime date, List<Long> petIds);
}
