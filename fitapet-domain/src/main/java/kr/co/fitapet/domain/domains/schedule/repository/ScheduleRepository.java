package kr.co.fitapet.domain.domains.schedule.repository;


import kr.co.fitapet.domain.common.repository.ExtendedRepository;
import kr.co.fitapet.domain.domains.schedule.domain.Schedule;

import java.util.List;

public interface ScheduleRepository extends ExtendedRepository<Schedule, Long>, ScheduleQueryRepository {
    List<Schedule> findAllById(Long id);
}
