package kr.co.fitapet.domain.domains.care.repository;


import kr.co.fitapet.domain.common.repository.ExtendedRepository;
import kr.co.fitapet.domain.domains.care.domain.Care;

public interface CareRepository extends ExtendedRepository<Care, Long>, CareQueryDslRepository {
    boolean existsByCareCategory_Id(Long categoryId);
}
