package kr.co.fitapet.api.apis.care.usecase;


import kr.co.fitapet.common.annotation.UseCase;
import kr.co.fitapet.common.execption.GlobalErrorException;
import kr.co.fitapet.domain.domains.care.domain.Care;
import kr.co.fitapet.domain.domains.care.domain.CareCategory;
import kr.co.fitapet.domain.domains.care.domain.CareDate;
import kr.co.fitapet.domain.domains.care.dto.CareCategoryInfo;
import kr.co.fitapet.domain.domains.care.dto.CareInfoRes;
import kr.co.fitapet.domain.domains.care.dto.CareSaveReq;
import kr.co.fitapet.domain.domains.care.exception.CareErrorCode;
import kr.co.fitapet.domain.domains.care.service.CareSearchService;
import kr.co.fitapet.domain.domains.care.service.CareSaveService;
import kr.co.fitapet.domain.domains.care.type.WeekType;
import kr.co.fitapet.domain.domains.care_log.domain.CareLog;
import kr.co.fitapet.domain.domains.care_log.dto.CareLogInfo;
import kr.co.fitapet.domain.domains.care_log.exception.CareLogErrorCode;
import kr.co.fitapet.domain.domains.care_log.service.CareLogSearchService;
import kr.co.fitapet.domain.domains.care_log.service.CareLogSaveService;
import kr.co.fitapet.domain.domains.manager.service.ManagerSearchService;
import kr.co.fitapet.domain.domains.member.service.MemberSearchService;
import kr.co.fitapet.domain.domains.pet.domain.Pet;
import kr.co.fitapet.domain.domains.pet.exception.PetErrorCode;
import kr.co.fitapet.domain.domains.pet.service.PetSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@UseCase
@RequiredArgsConstructor
@Slf4j
public class CareUseCase {
    private final CareSaveService careSaveService;

    private final MemberSearchService memberSearchService;
    private final ManagerSearchService managerSearchService;
    private final PetSearchService petSearchService;
    private final CareSearchService careSearchService;
    private final CareLogSearchService careLogSearchService;
    private final CareLogSaveService careLogSaveService;

    @Transactional
    public void saveCare(Long userId, Long petId, CareSaveReq.Request request) {
        CareSaveReq.CategoryDto categoryDto = request.category();
        CareSaveReq.CareInfoDto careInfoDto = request.care();
        List<CareSaveReq.AdditionalPetDto> additionalPetDtos = request.pets();

        List<Long> petIds = additionalPetDtos.stream().map(CareSaveReq.AdditionalPetDto::petId).toList();
        if (!managerSearchService.isManagerAll(userId, petIds)) {
            throw new GlobalErrorException(PetErrorCode.NOT_MANAGER_PET);
        }

        if (!petIds.contains(petId))
            additionalPetDtos.add(CareSaveReq.AdditionalPetDto.of(petId, categoryDto.categoryId()));
        petSearchService.findPetsByIds(petIds);

        persistAboutCare(categoryDto, careInfoDto, additionalPetDtos);
    }

    @Transactional
    public List<?> findCareCategoryNamesByPetId(Long petId) {
         List<CareCategory> careCategories = careSearchService.findAllCareCategoriesByPetId(petId);
         return CareCategoryInfo.from(careCategories).getCareCategorySummaries();
    }

    @Transactional(readOnly = true)
    public CareInfoRes findCaresByPetId(Long petId) {
        List<CareCategory> careCategories = careSearchService.findAllCareCategoriesByPetId(petId);
        List<CareInfoRes.CareCategoryDto> careCategoryDtos = new ArrayList<>();

        for (CareCategory careCategory : careCategories) {
            List<Care> cares = careCategory.getCares();
            List<CareInfoRes.CareDto> careDtos = new ArrayList<>();

            WeekType todayWeek = WeekType.fromLegacyType(LocalDateTime.now().getDayOfWeek().toString());
            LocalDateTime today = LocalDateTime.now();

            for (Care care : cares) {
                List<CareDate> careDates = careSearchService.findCareDatesFromCareIdAndWeek(care.getId(), todayWeek);

                for (CareDate careDate : careDates) {
                    boolean isClear = careLogSearchService.existsByCareDateIdOnLogDate(careDate.getId(), today);
                    careDtos.add(CareInfoRes.CareDto.of(care.getId(), careDate.getId(), care.getCareName(), careDate.getCareTime(), isClear));
                }
            }
            if (!careDtos.isEmpty())
                careCategoryDtos.add(CareInfoRes.CareCategoryDto.of(careCategory.getId(), careCategory.getCategoryName(), careDtos));
        }
        return CareInfoRes.from(careCategoryDtos);
    }

    @Transactional
    public CareLogInfo doCare(Long userId, Long careDateId) {
        CareDate careDate = careSearchService.findCareDateById(careDateId);

        if (!careDate.getWeek().checkToday()) {
            log.warn("오늘 날짜에 대한 요청이 아닙니다. {} <- 요청 : {}", careDate.getWeek(), LocalDateTime.now().getDayOfWeek());
            throw new GlobalErrorException(CareErrorCode.NOT_TODAY_CARE);
        }

        LocalDateTime today = LocalDateTime.now();
        if (careLogSearchService.existsByCareDateIdOnLogDate(careDateId, today)) {
            log.warn("이미 케어를 수행한 기록이 존재합니다.");
            throw new GlobalErrorException(CareErrorCode.ALREADY_CARED);
        }

        CareLog careLog = careLogSaveService.save(CareLog.of(careDate));
        log.info("careLog: {}", careLog);
        return CareLogInfo.of(careLog.getLogDate(), memberSearchService.findById(userId).getUid());
    }

    @Transactional
    public void cancelCare(Long careDateId) {
        CareDate careDate = careSearchService.findCareDateById(careDateId);
        LocalDateTime today = LocalDateTime.now();

        if (!careDate.getWeek().checkToday()) {
            log.warn("오늘 날짜에 대한 요청이 아닙니다. {} <- 요청 : {}", careDate.getWeek(), LocalDateTime.now().getDayOfWeek());
            throw new GlobalErrorException(CareErrorCode.NOT_TODAY_CARE);
        }

        CareLog careLog = careLogSearchService.findByCareDateIdOnLogDate(careDateId, today);
        careLogSaveService.delete(careLog);
    }

    @Transactional
    public void deleteCare(Long careId) {
        Care care = careSearchService.findCareById(careId);
        CareCategory careCategory = care.getCareCategory();

        careSaveService.deleteCare(care);

        if (!careSearchService.existsCareUnderCategory(careCategory.getId())) {
            careSaveService.deleteCareCategory(careCategory);
        }
    }

    private void persistAboutCare(
            CareSaveReq.CategoryDto categoryDto,
            CareSaveReq.CareInfoDto careInfoDto,
            List<CareSaveReq.AdditionalPetDto> additionalPetDtos
    ) {
        List<CareCategory> categories = findOrCreateCategories(categoryDto, additionalPetDtos);
        careSaveService.saveCareCategories(categories);

        List<Care> cares = createCares(categoryDto, careInfoDto, categories);
        careSaveService.saveCares(cares);

        List<CareDate> dates = createCareDates(careInfoDto, cares);
        careSaveService.saveCareDates(dates);
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
