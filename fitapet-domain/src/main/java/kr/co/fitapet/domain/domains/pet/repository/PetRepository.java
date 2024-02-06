package kr.co.fitapet.domain.domains.pet.repository;


import kr.co.fitapet.domain.common.repository.ExtendedJpaRepository;
import kr.co.fitapet.domain.domains.pet.domain.Pet;

public interface PetRepository extends ExtendedJpaRepository<Pet, Long> {
    boolean existsById(Long id);
}
