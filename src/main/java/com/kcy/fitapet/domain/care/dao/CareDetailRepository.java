package com.kcy.fitapet.domain.care.dao;

import com.kcy.fitapet.domain.care.domain.CareDate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CareDetailRepository extends JpaRepository<CareDate, Long> {
}
