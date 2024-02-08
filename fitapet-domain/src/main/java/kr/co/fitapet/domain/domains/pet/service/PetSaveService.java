package kr.co.fitapet.domain.domains.pet.service;

import kr.co.fitapet.common.annotation.DomainService;
import kr.co.fitapet.domain.domains.pet.domain.Pet;
import kr.co.fitapet.domain.domains.pet.domain.PetSchedule;
import kr.co.fitapet.domain.domains.pet.repository.PetRepository;
import kr.co.fitapet.domain.domains.pet.repository.PetScheduleRepository;
import kr.co.fitapet.domain.domains.schedule.domain.Schedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@DomainService
@RequiredArgsConstructor
public class PetSaveService {
    private final PetRepository petRepository;
    private final PetScheduleRepository petScheduleRepository;

    @Transactional
    public Pet savePet(Pet pet) {;
        return petRepository.save(pet);
    }

    @Transactional
    public void mappingAllPetAndSchedule(List<Pet> pets, Schedule schedule) {
        List<PetSchedule> schedules = new ArrayList<>();
        for (Pet pet : pets) {
            schedules.add(PetSchedule.of(pet, schedule));
        }
    }
}
