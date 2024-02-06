package kr.co.fitapet.domain.domains.schedule.repository;


import kr.co.fitapet.domain.common.repository.ExtendedJpaRepository;
import kr.co.fitapet.domain.domains.schedule.domain.Schedule;

import java.util.List;

public interface ScheduleJpaRepository extends ExtendedJpaRepository<Schedule, Long>, ScheduleQueryRepository {
    public List<Schedule> findAllById(Long id);
}
