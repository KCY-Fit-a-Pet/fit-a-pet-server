package kr.co.fitapet.domain.domains.manager.repository;

public interface ManagerQueryDslRepository {
    Long findMasterIdByPetId(Long petId);
}
