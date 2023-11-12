package com.kcy.fitapet.domain.pet.dao;

import com.kcy.fitapet.domain.pet.domain.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {
}
