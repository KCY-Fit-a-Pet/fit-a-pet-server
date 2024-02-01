package com.kcy.fitapet.domain.pet.dao;

import com.kcy.fitapet.domain.pet.domain.Pet;
import com.kcy.fitapet.global.common.repository.ExtendedJpaRepository;

public interface PetRepository extends ExtendedJpaRepository<Pet, Long> {
    boolean existsById(Long id);
}
