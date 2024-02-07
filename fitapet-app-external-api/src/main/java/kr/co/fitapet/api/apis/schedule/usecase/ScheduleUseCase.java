package kr.co.fitapet.api.apis.schedule.usecase;

import kr.co.fitapet.common.annotation.UseCase;
import kr.co.fitapet.common.execption.GlobalErrorException;
import kr.co.fitapet.domain.domains.member.service.MemberSearchService;
import kr.co.fitapet.domain.domains.pet.domain.Pet;
import kr.co.fitapet.domain.domains.pet.exception.PetErrorCode;
import kr.co.fitapet.domain.domains.pet.service.PetSaveService;
import kr.co.fitapet.domain.domains.pet.service.PetSearchService;
import kr.co.fitapet.domain.domains.schedule.domain.Schedule;
import kr.co.fitapet.domain.domains.schedule.dto.ScheduleInfoDto;
import kr.co.fitapet.domain.domains.schedule.dto.ScheduleSaveDto;
import kr.co.fitapet.domain.domains.schedule.service.ScheduleSaveService;
import kr.co.fitapet.domain.domains.schedule.service.ScheduleSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@UseCase
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
