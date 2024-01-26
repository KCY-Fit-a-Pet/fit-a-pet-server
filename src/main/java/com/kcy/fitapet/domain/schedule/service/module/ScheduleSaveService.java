package com.kcy.fitapet.domain.schedule.service.module;

import com.kcy.fitapet.domain.schedule.dao.ScheduleJpaRepository;
import com.kcy.fitapet.domain.schedule.domain.Schedule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleSaveService {
    private final ScheduleJpaRepository scheduleRepository;

    public Schedule saveSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }
}
