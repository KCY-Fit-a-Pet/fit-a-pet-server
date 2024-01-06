package com.kcy.fitapet.domain.care.dao;

import com.kcy.fitapet.domain.care.domain.CareCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CareCategoryRepository extends JpaRepository<CareCategory, Long> {
}
