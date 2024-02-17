package kr.co.fitapet.domain.domains.manager.repository;


import kr.co.fitapet.domain.common.repository.ExtendedRepository;
import kr.co.fitapet.domain.domains.manager.domain.Manager;
import kr.co.fitapet.domain.domains.manager.type.ManageType;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ManagerRepository extends ExtendedRepository<Manager, Long>, ManagerQueryDslRepository {
    boolean existsByMember_IdAndPet_Id(Long memberId, Long petId);
    List<Manager> findAllByMember_Id(Long memberId);
    Manager findByMember_IdAndPet_Id(Long memberId, Long petId);
}
