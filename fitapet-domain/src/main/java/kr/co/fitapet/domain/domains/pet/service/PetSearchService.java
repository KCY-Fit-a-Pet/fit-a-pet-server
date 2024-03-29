package kr.co.fitapet.domain.domains.pet.service;

import kr.co.fitapet.common.annotation.DomainService;
import kr.co.fitapet.domain.domains.care.repository.CareCategoryRepository;
import kr.co.fitapet.domain.domains.pet.domain.Pet;
import kr.co.fitapet.domain.domains.pet.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@DomainService
@RequiredArgsConstructor
public class PetSearchService {
    private final PetRepository petRepository;
    private final CareCategoryRepository careCategoryRepository;

    @Transactional(readOnly = true)
    public Pet findPetById(Long id) {
        return petRepository.findByIdOrElseThrow(id);
    }

    @Transactional(readOnly = true)
    public List<Pet> findPetsByIds(List<Long> petIds) {
        return petRepository.findAllById(petIds);
    }

    @Transactional(readOnly = true)
    public boolean isExistPetById(Long petId) {
        return petRepository.existsById(petId);
    }

    @Transactional(readOnly = true)
    public boolean isExistPetByIds(List<Long> petIds) {
        for (Long petId : petIds) {
            if (!petRepository.existsById(petId)) return false;
        }
        return true;
    }
}
