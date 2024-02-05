package kr.co.fitapet.domain.domains.care.repository;

import com.kcy.fitapet.domain.care.domain.CareDate;
import com.kcy.fitapet.domain.care.type.WeekType;
import com.kcy.fitapet.global.common.repository.ExtendedJpaRepository;

import java.util.List;

public interface CareDateRepository extends ExtendedJpaRepository<CareDate, Long> {
    List<CareDate> findAllByCare_IdAndWeek(Long careId, WeekType week);
    boolean existsByIdAndCare_Id(Long careDateId, Long careId);
}
