package com.kcy.fitapet.domain.schedule.dao;

import com.kcy.fitapet.domain.schedule.domain.Schedule;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleQueryRepository {
    List<Schedule> findTopCountScheduleByIdOnDate(Long id, LocalDateTime scheduleDate, Integer count);
}
