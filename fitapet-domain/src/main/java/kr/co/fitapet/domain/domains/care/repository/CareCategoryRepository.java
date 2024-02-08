package kr.co.fitapet.domain.domains.care.repository;


import kr.co.fitapet.domain.common.repository.ExtendedJpaRepository;
import kr.co.fitapet.domain.domains.care.domain.CareCategory;

import java.util.List;

public interface CareCategoryRepository extends ExtendedJpaRepository<CareCategory, Long> {
    List<CareCategory> findAllByPet_Id(Long petId);
}
