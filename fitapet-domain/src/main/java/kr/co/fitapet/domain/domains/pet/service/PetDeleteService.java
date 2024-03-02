package kr.co.fitapet.domain.domains.pet.service;

import kr.co.fitapet.common.annotation.DomainService;
import kr.co.fitapet.domain.domains.pet.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@DomainService
@RequiredArgsConstructor
public class PetDeleteService {
    private final PetRepository petRepository;

    @Transactional
    public void deletePet(Long petId) {
        petRepository.deleteById(petId);
    }
}
