package com.kcy.fitapet.domain.schedule.service.module;

import com.kcy.fitapet.domain.schedule.dao.ScheduleQueryRepository;
import com.kcy.fitapet.domain.schedule.dao.ScheduleRepository;
import com.kcy.fitapet.domain.schedule.domain.Schedule;
import com.kcy.fitapet.domain.schedule.dto.ScheduleInfoDto;
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

    /**
     * 오늘 날짜의 현재 시간 이후에 해당하는 스케줄 조회
     * @param petId 반려동물 id
     * @param scheduleDate 조회할 날짜
     * @param count 조회할 스케줄 개수
     * @return 조회된 스케줄 리스트
     */
    // TODO: 현재 시간까지 고려
    @Transactional(readOnly = true)
    public List<Schedule> findSchedules(Long petId, LocalDateTime scheduleDate, Integer count) {
        return scheduleQueryRepository.findTopCountScheduleByIdOnDate(petId, scheduleDate, count);
    }

    @Transactional(readOnly = true)
    public List<ScheduleInfoDto.ScheduleInfo> findSchedulesByCalender(
            LocalDateTime date,
            List<Long> petIds
    ) {
        return scheduleQueryRepository.findSchedulesByCalender(date, petIds);
    }
}
