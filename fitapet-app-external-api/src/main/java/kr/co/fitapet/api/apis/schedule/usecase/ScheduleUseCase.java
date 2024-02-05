package kr.co.fitapet.api.apis.schedule.usecase;

import com.kcy.fitapet.domain.member.service.module.MemberSearchService;
import com.kcy.fitapet.domain.pet.domain.Pet;
import com.kcy.fitapet.domain.pet.exception.PetErrorCode;
import com.kcy.fitapet.domain.pet.service.module.PetSaveService;
import com.kcy.fitapet.domain.pet.service.module.PetSearchService;
import com.kcy.fitapet.domain.schedule.domain.Schedule;
import com.kcy.fitapet.domain.schedule.dto.ScheduleInfoDto;
import com.kcy.fitapet.domain.schedule.dto.ScheduleSaveDto;
import com.kcy.fitapet.domain.schedule.service.module.ScheduleSaveService;
import com.kcy.fitapet.domain.schedule.service.module.ScheduleSearchService;
import com.kcy.fitapet.global.common.response.exception.GlobalErrorException;
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
public class ScheduleUseCase {
    private final MemberSearchService memberSearchService;

    private final ScheduleSearchService scheduleSearchService;
    private final ScheduleSaveService scheduleSaveService;
    private final PetSearchService petSearchService;
    private final PetSaveService petSaveService;

    @Transactional
    public void saveSchedule(Long userId, ScheduleSaveDto.Request request) {
        List<Long> petIds = request.petIds();
        if (!memberSearchService.isManagerAll(userId, petIds))
            throw new GlobalErrorException(PetErrorCode.NOT_MANAGER_PET);

        Schedule schedule = scheduleSaveService.saveSchedule(request.toEntity());

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
}
