package com.kcy.fitapet.domain.pet.service.module;

import com.kcy.fitapet.domain.pet.dao.PetJpaRepository;
import com.kcy.fitapet.domain.pet.dao.PetScheduleJpaRepository;
import com.kcy.fitapet.domain.pet.domain.Pet;
import com.kcy.fitapet.domain.pet.domain.PetSchedule;
import com.kcy.fitapet.domain.schedule.domain.Schedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PetSaveService {
    private final PetJpaRepository petRepository;
    private final PetScheduleJpaRepository petScheduleRepository;

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
