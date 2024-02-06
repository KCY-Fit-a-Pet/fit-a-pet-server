package kr.co.fitapet.domain.domains.care.repository;

import kr.co.fitapet.domain.common.repository.ExtendedJpaRepository;
import kr.co.fitapet.domain.domains.care.domain.CareDate;
import kr.co.fitapet.domain.domains.care.type.WeekType;

import java.util.List;

public interface CareDateRepository extends ExtendedJpaRepository<CareDate, Long> {
    List<CareDate> findAllByCare_IdAndWeek(Long careId, WeekType week);
    boolean existsByIdAndCare_Id(Long careDateId, Long careId);
}
