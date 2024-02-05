package kr.co.fitapet.domain.domains.schedule.repository;

import com.kcy.fitapet.domain.schedule.dto.ScheduleInfoDto;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleQueryRepository {
    /**
     * pet_id로 반려동물이 등록된 해당 날짜의 schedule_id 리스트 조회
     * @param petId
     * @param date : 탐색할 날짜, 만약 지금 시간 이후의 스케줄을 조회하고 싶다면 LocalDateTime.now()를 넣어주면 된다. (전체라면 LocalDate.startOf())
     */
    List<Long> findScheduleIds(Long petId, LocalDateTime date);

    /**
     * pet_id로 반려동물이 등록된 해당 날짜의 schedule_id 리스트를 count 만큼 조회 <br/>
     */
    List<Long> findScheduleIds(Long petId, LocalDateTime date, int count);

    /**
     * schedule_id로 스케줄 및 참여 반려동물 리스트 조회
     */
    List<ScheduleInfoDto.ScheduleInfo> findSchedulesByIds(List<Long> scheduleIds);

    /**
     * 해당 날짜의 스케줄 및 참여 반려동물들 리스트 조회
     */
    List<ScheduleInfoDto.ScheduleInfo> findSchedulesByCalender(LocalDateTime date, List<Long> petIds);
}
