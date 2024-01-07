package com.kcy.fitapet.domain.care.dao;

import com.kcy.fitapet.domain.care.domain.CareCategory;
import com.kcy.fitapet.global.common.repository.ExtendedRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CareCategoryRepository extends ExtendedRepository<CareCategory, Long> {
}
