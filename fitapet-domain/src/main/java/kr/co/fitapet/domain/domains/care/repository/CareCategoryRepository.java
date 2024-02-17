package kr.co.fitapet.domain.domains.care.repository;


import kr.co.fitapet.domain.common.repository.ExtendedJpaRepository;
import kr.co.fitapet.domain.common.repository.ExtendedRepository;
import kr.co.fitapet.domain.domains.care.domain.CareCategory;

import java.util.List;

public interface CareCategoryRepository extends ExtendedRepository<CareCategory, Long> {
    List<CareCategory> findAllByPet_Id(Long petId);
}
