package com.kcy.fitapet.domain.schedule.service.component;

import com.kcy.fitapet.domain.pet.domain.Pet;
import com.kcy.fitapet.domain.pet.service.module.PetSaveService;
import com.kcy.fitapet.domain.pet.service.module.PetSearchService;
import com.kcy.fitapet.domain.schedule.domain.Schedule;
import com.kcy.fitapet.domain.schedule.dto.ScheduleInfoDto;
import com.kcy.fitapet.domain.schedule.dto.ScheduleSaveDto;
import com.kcy.fitapet.domain.schedule.service.module.ScheduleSaveService;
import com.kcy.fitapet.domain.schedule.service.module.ScheduleSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScheduleManageService {
    private final ScheduleSearchService scheduleSearchService;
    private final ScheduleSaveService scheduleSaveService;
    private final PetSearchService petSearchService;
    private final PetSaveService petSaveService;

    @Transactional
    public void saveSchedule(Long petId, ScheduleSaveDto.Request request) {
        Schedule schedule = scheduleSaveService.saveSchedule(request.toEntity());

        List<Long> petIds = request.petIds();
        if (!petIds.contains(petId))
            petIds.add(petId);

        List<Pet> pets = petSearchService.findPetsByIds(petIds);
        petSaveService.mappingAllPetAndSchedule(pets, schedule);
    }

    @Transactional(readOnly = true)
    public ScheduleInfoDto findPetSchedules(Long petId, Integer count) {
        LocalDateTime now = LocalDateTime.now();

        List<Schedule> schedules = (count != null)
                ? scheduleSearchService.findSchedules(petId, now, count)
                : scheduleSearchService.findSchedules(petId);

        List<ScheduleInfoDto.ScheduleInfo> scheduleInfo = schedules.stream()
                .map(schedule -> ScheduleInfoDto.ScheduleInfo.from(schedule, new ArrayList<>())).toList();
        return ScheduleInfoDto.of(scheduleInfo);
    }
}
