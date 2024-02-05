package kr.co.fitapet.domain.domains.care.repository;

import com.kcy.fitapet.domain.care.domain.CareCategory;
import com.kcy.fitapet.global.common.repository.ExtendedJpaRepository;

import java.util.List;

public interface CareCategoryRepository extends ExtendedJpaRepository<CareCategory, Long> {
    List<CareCategory> findAllByPet_Id(Long petId);
}
