package kr.co.fitapet.domain.domains.schedule.service;

import kr.co.fitapet.common.annotation.DomainService;
import kr.co.fitapet.domain.domains.schedule.domain.Schedule;
import kr.co.fitapet.domain.domains.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@DomainService
@RequiredArgsConstructor
public class ScheduleSaveService {
    private final ScheduleRepository scheduleRepository;

    public Schedule saveSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }
}
