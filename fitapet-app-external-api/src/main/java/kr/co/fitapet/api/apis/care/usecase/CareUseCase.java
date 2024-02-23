package kr.co.fitapet.api.apis.care.usecase;


import kr.co.fitapet.api.apis.care.mapper.CareMapper;
import kr.co.fitapet.common.annotation.UseCase;
import kr.co.fitapet.common.execption.GlobalErrorException;
import kr.co.fitapet.domain.domains.care.domain.Care;
import kr.co.fitapet.domain.domains.care.domain.CareCategory;
import kr.co.fitapet.api.apis.care.dto.CareCategoryInfo;
import kr.co.fitapet.api.apis.care.dto.CareInfoRes;
import kr.co.fitapet.api.apis.care.dto.CareSaveReq;
import kr.co.fitapet.domain.domains.care.service.CareSearchService;
import kr.co.fitapet.domain.domains.care_log.domain.CareLog;
import kr.co.fitapet.domain.domains.care_log.dto.CareLogInfo;
import kr.co.fitapet.domain.domains.manager.service.ManagerSearchService;
import kr.co.fitapet.domain.domains.member.service.MemberSearchService;
import kr.co.fitapet.domain.domains.pet.exception.PetErrorCode;
import kr.co.fitapet.domain.domains.pet.service.PetSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@UseCase
@RequiredArgsConstructor
@Slf4j
public class CareUseCase {
    private final MemberSearchService memberSearchService;
    private final ManagerSearchService managerSearchService;
    private final PetSearchService petSearchService;
    private final CareSearchService careSearchService;

    private final CareMapper careMapper;

    @Transactional
    public void saveCare(Long userId, Long petId, CareSaveReq.Request request) {
        List<CareSaveReq.AdditionalPetDto> additionalPetDtos = request.pets();

        List<Long> petIds = additionalPetDtos.stream().map(CareSaveReq.AdditionalPetDto::petId).toList();
        if (!managerSearchService.isManagerAll(userId, petIds)) {
            throw new GlobalErrorException(PetErrorCode.NOT_MANAGER_PET);
        }

        if (!petIds.contains(petId))
            additionalPetDtos.add(CareSaveReq.AdditionalPetDto.of(petId, request.category().categoryId()));
        petSearchService.findPetsByIds(petIds);

        careMapper.saveCare(request.category(), request.care(), additionalPetDtos);
    }

    @Transactional
    public List<?> findCareCategoryNamesByPetId(Long petId) {
         List<CareCategory> careCategories = careSearchService.findCareCategoriesByPetId(petId);
         return CareCategoryInfo.from(careCategories).getCareCategorySummaries();
    }

    @Transactional(readOnly = true)
    public CareInfoRes findCaresByPetId(Long petId) {
        List<CareCategory> careCategories = careSearchService.findCareCategoriesByPetId(petId);
        LocalDateTime now = LocalDateTime.now();

        return CareInfoRes.from(careCategories.stream()
                .map(careCategory -> careMapper.mapToCareCategoryDto(careCategory, now))
                .filter(careCategoryDto -> !careCategoryDto.cares().isEmpty())
                .toList());
    }

    @Transactional
    public void updateCare(Long careId, CareSaveReq.UpdateRequest request) {
        Care care = careSearchService.findCareById(careId);
        care.updateCare(request.care().careName(), request.care().limitTime());

        CareCategory category = care.getCareCategory();
        careMapper.updateCareCategory(care, category, request.category());
        careMapper.updateCareDates(careId, request.care());
    }

    @Transactional
    public CareLogInfo doCare(Long userId, Long careDateId) {
        CareLog careLog = careMapper.doCare(careDateId);
        return CareLogInfo.of(careLog.getLogDate(), memberSearchService.findById(userId).getUid());
    }

    @Transactional
    public void cancelCare(Long careDateId) {
        careMapper.cancelCare(careDateId);
    }

    @Transactional
    public void deleteCare(Long careId) {
        Care care = careSearchService.findCareById(careId);
        careMapper.deleteCare(care);
    }
}
