package kr.co.fitapet.domain.domains.schedule.repository;

import com.kcy.fitapet.domain.schedule.domain.Schedule;
import com.kcy.fitapet.global.common.repository.ExtendedJpaRepository;

import java.util.List;

public interface ScheduleJpaRepository extends ExtendedJpaRepository<Schedule, Long>, ScheduleQueryRepository {
    public List<Schedule> findAllById(Long id);
}
