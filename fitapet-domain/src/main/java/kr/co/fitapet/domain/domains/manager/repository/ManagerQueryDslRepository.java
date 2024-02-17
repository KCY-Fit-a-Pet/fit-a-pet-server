package kr.co.fitapet.domain.domains.manager.repository;

import kr.co.fitapet.domain.domains.manager.dto.ManagerInfoRes;

import java.util.List;

public interface ManagerQueryDslRepository {
    Long findMasterIdByPetId(Long petId);
    List<ManagerInfoRes> findAllManager(Long petId, Long memberId);
}
