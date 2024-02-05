package kr.co.fitapet.domain.domains.pet.service;

import com.kcy.fitapet.domain.care.dao.CareCategoryRepository;
import com.kcy.fitapet.domain.pet.dao.PetRepository;
import com.kcy.fitapet.domain.pet.domain.Pet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
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
