package com.kcy.fitapet.domain.schedule.service.module;

import com.kcy.fitapet.domain.schedule.dao.ScheduleQueryRepository;
import com.kcy.fitapet.domain.schedule.dao.ScheduleRepository;
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

    /**
     * pet_id로 반려동물이 등록된 해당 날짜의 schedule_id 리스트 조회
     * @param petId 반려동물 id
     * @param date : 탐색할 날짜, 만약 지금 시간 이후의 스케줄을 조회하고 싶다면 LocalDateTime.now()를 넣어주면 된다. (전체라면 LocalDate.startOf())
     * @param count : 조회할 스케줄 개수, -1이면 전체 조회
     */
    @Transactional(readOnly = true)
    public List<Long> findScheduleIdsByPetIdAfterDateTime(Long petId, LocalDateTime date, int count) {
        return (count != -1) ? scheduleRepository.findScheduleIds(petId, date, count)
                             : scheduleRepository.findScheduleIds(petId, date);
    }

    /**
     * schedule_id로 스케줄 및 참여 반려동물 리스트 조회
     */
    @Transactional(readOnly = true)
    public List<ScheduleInfoDto.ScheduleInfo> findTopCountSchedulesByIds(List<Long> scheduleIds) {
        return scheduleRepository.findSchedulesByIds(scheduleIds);
    }

    @Transactional(readOnly = true)
    public List<ScheduleInfoDto.ScheduleInfo> findSchedulesByCalender(
            LocalDateTime date,
            List<Long> petIds
    ) {
        return scheduleRepository.findSchedulesByCalender(date, petIds);
    }
}
