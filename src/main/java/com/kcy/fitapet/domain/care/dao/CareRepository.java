package com.kcy.fitapet.domain.care.dao;

import com.kcy.fitapet.domain.care.domain.Care;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CareRepository extends JpaRepository<Care, Long> {
}
