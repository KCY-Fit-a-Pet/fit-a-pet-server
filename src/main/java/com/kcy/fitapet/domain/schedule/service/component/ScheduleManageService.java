package com.kcy.fitapet.domain.schedule.service.component;

import com.kcy.fitapet.domain.schedule.domain.Schedule;
import com.kcy.fitapet.domain.schedule.dto.ScheduleSaveDto;
import com.kcy.fitapet.domain.schedule.service.module.ScheduleSaveService;
import com.kcy.fitapet.domain.schedule.service.module.ScheduleSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScheduleManageService {
    private final ScheduleSearchService scheduleSearchService;
    private final ScheduleSaveService scheduleSaveService;
    private final PetScheduleSaveService petScheduleSaveService;

    public void saveSchedule(Long petId, ScheduleSaveDto.Request request) {
        Schedule schedule = scheduleSaveService.saveSchedule(request.toEntity());

        List<Long> petIds = request.petIds();
        if (!petIds.contains(petId))
            petIds.add(petId);

        petScheduleSaveService.mappingAllPetAndSchedule(petIds, schedule);
    }
}
