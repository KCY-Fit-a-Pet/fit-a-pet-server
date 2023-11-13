package com.kcy.fitapet.domain.pet.service.module;

import com.kcy.fitapet.domain.pet.dao.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PetSearchService {
    private final PetRepository petRepository;

    @Transactional(readOnly = true)
    public boolean isExistPetById(Long petId) {
        return petRepository.existsById(petId);
    }
}
