package com.kcy.fitapet.domain.schedule.service.module;

import com.kcy.fitapet.domain.schedule.dao.ScheduleQueryRepository;
import com.kcy.fitapet.domain.schedule.dao.ScheduleRepository;
import com.kcy.fitapet.domain.schedule.domain.Schedule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScheduleSearchService {
    private final ScheduleRepository scheduleRepository;
    private final ScheduleQueryRepository scheduleQueryRepository;

    @Transactional(readOnly = true)
    public List<Schedule> findSchedules(Long petId) {
        return scheduleRepository.findAllById(petId);
    }

    @Transactional(readOnly = true)
    public List<Schedule> findSchedules(Long petId, LocalDateTime scheduleDate, Integer count) {
        return scheduleQueryRepository.findTopCountScheduleByIdOnDate(petId, scheduleDate, count);
    }
}
