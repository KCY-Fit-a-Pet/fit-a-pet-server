package com.kcy.fitapet.domain.care.dao;

import com.kcy.fitapet.domain.care.domain.CareDate;
import com.kcy.fitapet.domain.care.type.WeekType;
import com.kcy.fitapet.global.common.repository.ExtendedRepository;

import java.util.List;

public interface CareDateRepository extends ExtendedRepository<CareDate, Long> {
    List<CareDate> findAllByCare_IdAndWeek(Long careId, WeekType week);
}
