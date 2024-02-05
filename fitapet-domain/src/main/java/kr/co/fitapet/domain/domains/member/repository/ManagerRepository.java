package kr.co.fitapet.domain.domains.member.repository;

import com.kcy.fitapet.domain.member.domain.Manager;
import com.kcy.fitapet.global.common.repository.ExtendedJpaRepository;

import java.util.List;

public interface ManagerRepository extends ExtendedJpaRepository<Manager, Long> {
    boolean existsByMember_IdAndPet_Id(Long memberId, Long petId);
    List<Manager> findAllByMember_Id(Long memberId);
}
