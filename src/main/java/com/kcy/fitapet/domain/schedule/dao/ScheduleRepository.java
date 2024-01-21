package com.kcy.fitapet.domain.schedule.dao;

import com.kcy.fitapet.domain.schedule.domain.Schedule;
import com.kcy.fitapet.global.common.repository.ExtendedRepository;

import java.util.List;

public interface ScheduleRepository extends ExtendedRepository<Schedule, Long> {
    public List<Schedule> findAllById(Long id);
}
