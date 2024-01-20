package com.kcy.fitapet.domain.schedule.service.module;

import com.kcy.fitapet.domain.schedule.dao.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScheduleSearchService {
    private final ScheduleRepository scheduleRepository;
}
