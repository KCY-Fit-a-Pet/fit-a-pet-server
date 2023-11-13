package com.kcy.fitapet.domain.pet.service.module;

import com.kcy.fitapet.domain.pet.dao.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PetSearchService {
    private final PetRepository petRepository;

    public boolean isExistPetById(Long petId) {
        return petRepository.existsById(petId);
    }
}
