package com.kcy.fitapet.domain.pet.dao;

import com.kcy.fitapet.domain.pet.domain.PetCare;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetCareRepository extends JpaRepository<PetCare, Long> {
}
