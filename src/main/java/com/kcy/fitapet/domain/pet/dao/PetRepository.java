package com.kcy.fitapet.domain.pet.dao;

import com.kcy.fitapet.domain.pet.domain.Pet;
import com.kcy.fitapet.global.common.repository.ExtendedRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends ExtendedRepository<Pet, Long> {
}
