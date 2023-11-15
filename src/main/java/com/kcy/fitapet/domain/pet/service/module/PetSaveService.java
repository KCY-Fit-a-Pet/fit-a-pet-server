package com.kcy.fitapet.domain.pet.service.module;

import com.kcy.fitapet.domain.care.domain.Care;
import com.kcy.fitapet.domain.pet.dao.PetCareRepository;
import com.kcy.fitapet.domain.pet.dao.PetRepository;
import com.kcy.fitapet.domain.pet.domain.Pet;
import com.kcy.fitapet.domain.pet.domain.PetCare;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PetSaveService {
    private final PetRepository petRepository;
    private final PetCareRepository petCareRepository;

    @Transactional
    public Pet savePet(Pet pet) {;
        return petRepository.save(pet);
    }
}
