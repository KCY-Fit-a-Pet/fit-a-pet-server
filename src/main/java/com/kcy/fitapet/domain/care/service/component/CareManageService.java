package com.kcy.fitapet.domain.care.service.component;

import com.kcy.fitapet.domain.care.domain.Care;
import com.kcy.fitapet.domain.care.domain.CareCategory;
import com.kcy.fitapet.domain.care.domain.CareDate;
import com.kcy.fitapet.domain.care.dto.CareCategoryDto;
import com.kcy.fitapet.domain.care.dto.CareInfoRes;
import com.kcy.fitapet.domain.care.dto.CareSaveReq;
import com.kcy.fitapet.domain.care.exception.CareErrorCode;
import com.kcy.fitapet.domain.care.service.module.CareUpdateService;
import com.kcy.fitapet.domain.care.service.module.CareSearchService;
import com.kcy.fitapet.domain.care.type.WeekType;
import com.kcy.fitapet.domain.log.domain.CareLog;
import com.kcy.fitapet.domain.log.dto.CareLogInfo;
import com.kcy.fitapet.domain.log.service.CareLogUpdateService;
import com.kcy.fitapet.domain.log.service.CareLogSearchService;
import com.kcy.fitapet.domain.member.service.module.MemberSearchService;
import com.kcy.fitapet.domain.pet.domain.Pet;
import com.kcy.fitapet.domain.pet.exception.PetErrorCode;
import com.kcy.fitapet.domain.pet.service.module.PetSearchService;
import com.kcy.fitapet.global.common.response.exception.GlobalErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CareManageService {
    private final CareUpdateService careUpdateService;

    private final MemberSearchService memberSearchService;
    private final PetSearchService petSearchService;
    private final CareSearchService careSearchService;
    private final CareLogSearchService careLogSearchService;
    private final CareLogUpdateService careLogUpdateService;

    @Transactional
    public List<?> findCareCategoryNamesByPetId(Long petId) {
         List<CareCategory> careCategories = careSearchService.findAllCareCategoriesByPetId(petId);
         return CareCategoryDto.from(careCategories).getCareCategorySummaries();
    }

    @Transactional(readOnly = true)
    public CareInfoRes findCaresByPetId(Long petId) {
        List<CareCategory> careCategories = careSearchService.findAllCareCategoriesByPetId(petId);

        List<CareInfoRes.CareCategoryDto> careCategoryDtos = new ArrayList<>();
        for (CareCategory careCategory : careCategories) {
            log.info("careCategory: {}", careCategory);
            List<Care> cares = careCategory.getCares();

            List<CareInfoRes.CareDto> careDtos = new ArrayList<>();
            for (Care care : cares) {
                WeekType todayWeek = WeekType.fromLegacyType(LocalDateTime.now().getDayOfWeek().toString());
                List<CareDate> careDates = careSearchService.findCareDatesCareIdAndWeek(care.getId(), todayWeek);
                for (CareDate careDate : careDates) {
                    LocalDateTime today = LocalDateTime.now();

                    boolean isClear = careLogSearchService.existsByCareDateIdOnLogDate(careDate.getId(), today);
                    log.info("isClear: {}", isClear);

                    careDtos.add(CareInfoRes.CareDto.of(care.getId(), careDate.getId(), care.getCareName(), careDate.getCareTime(), isClear));
                }
            }

            careCategoryDtos.add(CareInfoRes.CareCategoryDto.of(careCategory.getId(), careCategory.getCategoryName(), careDtos));
        }
        return CareInfoRes.from(careCategoryDtos);
    }

    @Transactional
    public CareLogInfo doCare(Long careDateId, Long userId) {
        CareDate careDate = careSearchService.findCareDateById(careDateId);

        if (!careDate.getWeek().checkToday()) {
            log.warn("오늘 날짜에 대한 요청이 아닙니다. {} <- 요청 : {}", careDate.getWeek(), LocalDateTime.now().getDayOfWeek());
            throw new GlobalErrorException(CareErrorCode.NOT_TODAY_CARE);
        }

        // TODO: 케어 등록 시간 10분 전부터 수행할 수 있도록 조건 추가?
        LocalDateTime today = LocalDateTime.now();
        if (careLogSearchService.existsByCareDateIdOnLogDate(careDateId, today)) {
            log.warn("이미 케어를 수행한 기록이 존재합니다.");
            throw new GlobalErrorException(CareErrorCode.ALREADY_CARED);
        }

        CareLog careLog = careLogUpdateService.save(CareLog.of(careDate));
        log.info("careLog: {}", careLog);
        return CareLogInfo.of(careLog.getLogDate(), memberSearchService.findById(userId).getUid());
    }

    @Transactional
    public void saveCare(Long userId, Long petId, CareSaveReq.Request request) {
        CareSaveReq.CategoryDto categoryDto = request.category();
        CareSaveReq.CareInfoDto careInfoDto = request.care();
        List<CareSaveReq.AdditionalPetDto> additionalPetDtos = request.pets();

        List<Long> petIds = additionalPetDtos.stream().map(CareSaveReq.AdditionalPetDto::petId).toList();
        if (!memberSearchService.isManagerAll(userId, petIds)) {
            throw new GlobalErrorException(PetErrorCode.NOT_MANAGER_PET);
        }

        if (!petIds.contains(petId))
            additionalPetDtos.add(CareSaveReq.AdditionalPetDto.of(petId, categoryDto.categoryId()));
        petSearchService.findPetsByIds(petIds);

        persistAboutCare(categoryDto, careInfoDto, additionalPetDtos);
    }

    private void persistAboutCare(
            CareSaveReq.CategoryDto categoryDto,
            CareSaveReq.CareInfoDto careInfoDto,
            List<CareSaveReq.AdditionalPetDto> additionalPetDtos
    ) {
        List<CareCategory> categories = findOrCreateCategories(categoryDto, additionalPetDtos);
        careUpdateService.saveCareCategories(categories);

        List<Care> cares = createCares(categoryDto, careInfoDto, categories);
        careUpdateService.saveCares(cares);

        List<CareDate> dates = createCareDates(careInfoDto, cares);
        careUpdateService.saveCareDates(dates);
    }

    private List<CareCategory> findOrCreateCategories(
            CareSaveReq.CategoryDto categoryDto,
            List<CareSaveReq.AdditionalPetDto> additionalPetDtos
    ) {
        List<CareCategory> categories = new ArrayList<>(careSearchService.findAllCareCategoriesById(
                additionalPetDtos.stream().map(CareSaveReq.AdditionalPetDto::categoryId)
                        .filter(id -> !id.equals(0L)).toList()));

        additionalPetDtos.stream()
                .filter(dto -> dto.categoryId() == 0L)
                .map(dto -> {
                    Pet pet = petSearchService.findPetById(dto.petId());
                    CareCategory category = categoryDto.toCareCategory();
                    category.updatePet(pet);
                    return category;
                })
                .forEach(categories::add);

        return categories;
    }

    private List<Care> createCares(
            CareSaveReq.CategoryDto categoryDto,
            CareSaveReq.CareInfoDto careInfoDto,
            List<CareCategory> categories
    ) {
        return categories.stream()
                .map(category -> {
                    Care care = careInfoDto.toCare(category);
                    care.updateCareCategory(category);
                    return care;
                })
                .toList();
    }

    private List<CareDate> createCareDates(CareSaveReq.CareInfoDto careInfoDto, List<Care> cares) {
        return cares.stream()
                .flatMap(care -> careInfoDto.toCareDateEntity().stream()
                            .peek(date -> date.updateCare(care)))
                .toList();
    }
}
