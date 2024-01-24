package com.kcy.fitapet.domain.schedule.service.component;

import com.kcy.fitapet.domain.member.domain.Manager;
import com.kcy.fitapet.domain.member.service.module.MemberSearchService;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScheduleManageService {
    private final MemberSearchService memberSearchService;

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
    public ScheduleInfoDto findPetSchedules(Long petId, int count) {
        LocalDateTime now = (count != -1) ? LocalDateTime.now() : LocalDate.now().atStartOfDay();

        List<Long> scheduleIds = scheduleSearchService.findScheduleIdsByPetIdAfterDateTime(petId, now, count);
        log.info("scheduleIds: {}", scheduleIds);

        return ScheduleInfoDto.of(scheduleSearchService.findTopCountSchedulesByIds(scheduleIds));
    }

    @Transactional(readOnly = true)
    public ScheduleInfoDto findPetSchedules(Long userId, LocalDateTime date) {
        List<Pet> pets = memberSearchService.findAllManagerByMemberId(userId)
                .stream().map(Manager::getPet).toList();
        List<Long> petIds = pets.stream().map(Pet::getId).toList();

        List<ScheduleInfoDto.ScheduleInfo> scheduleInfo = scheduleSearchService.findSchedulesByCalender(date, petIds);
        return ScheduleInfoDto.of(scheduleInfo);
    }
}
