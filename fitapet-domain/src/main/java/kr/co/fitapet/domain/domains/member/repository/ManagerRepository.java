package kr.co.fitapet.domain.domains.member.repository;


import kr.co.fitapet.domain.common.repository.ExtendedJpaRepository;
import kr.co.fitapet.domain.domains.member.domain.Manager;

import java.util.List;

public interface ManagerRepository extends ExtendedJpaRepository<Manager, Long> {
    boolean existsByMember_IdAndPet_Id(Long memberId, Long petId);
    List<Manager> findAllByMember_Id(Long memberId);
}
