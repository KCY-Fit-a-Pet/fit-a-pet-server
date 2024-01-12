package com.kcy.fitapet.domain.care.dao;

import com.kcy.fitapet.domain.care.domain.CareCategory;
import com.kcy.fitapet.global.common.repository.ExtendedRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CareCategoryRepository extends ExtendedRepository<CareCategory, Long> {
    List<CareCategory> findAllByPet_Id(Long petId);
}
