package kr.co.fitapet.domain.domains.pet.repository;


import kr.co.fitapet.domain.common.repository.ExtendedRepository;
import kr.co.fitapet.domain.domains.pet.domain.Pet;

public interface PetRepository extends ExtendedRepository<Pet, Long> {
    boolean existsById(Long id);
}
