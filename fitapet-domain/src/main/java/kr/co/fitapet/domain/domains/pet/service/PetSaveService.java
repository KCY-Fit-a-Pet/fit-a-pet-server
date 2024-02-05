package kr.co.fitapet.domain.domains.pet.service;

import com.kcy.fitapet.domain.pet.dao.PetRepository;
import com.kcy.fitapet.domain.pet.dao.PetScheduleRepository;
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
