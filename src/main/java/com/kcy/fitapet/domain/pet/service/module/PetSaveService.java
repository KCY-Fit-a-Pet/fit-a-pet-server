package com.kcy.fitapet.domain.pet.service.module;

import com.kcy.fitapet.domain.pet.dao.PetRepository;
import com.kcy.fitapet.domain.pet.domain.Pet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PetSaveService {
    private final PetRepository petRepository;

    @Transactional
    public Pet savePet(Pet pet) {;
        return petRepository.save(pet);
    }
}
