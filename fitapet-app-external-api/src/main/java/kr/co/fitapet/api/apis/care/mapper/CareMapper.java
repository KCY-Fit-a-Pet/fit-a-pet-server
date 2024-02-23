package kr.co.fitapet.api.apis.care.mapper;

import kr.co.fitapet.api.apis.care.dto.CareSaveReq;
import kr.co.fitapet.common.annotation.Mapper;
import kr.co.fitapet.common.execption.GlobalErrorException;
import kr.co.fitapet.domain.domains.care.domain.Care;
import kr.co.fitapet.domain.domains.care.domain.CareCategory;
import kr.co.fitapet.domain.domains.care.domain.CareDate;
import kr.co.fitapet.api.apis.care.dto.CareInfoRes;
import kr.co.fitapet.domain.domains.care.exception.CareErrorCode;
import kr.co.fitapet.domain.domains.care.service.CareSaveService;
import kr.co.fitapet.domain.domains.care.service.CareSearchService;
import kr.co.fitapet.domain.domains.care.type.WeekType;
import kr.co.fitapet.domain.domains.care_log.domain.CareLog;
import kr.co.fitapet.domain.domains.care_log.service.CareLogSaveService;
import kr.co.fitapet.domain.domains.care_log.service.CareLogSearchService;
import kr.co.fitapet.domain.domains.pet.domain.Pet;
import kr.co.fitapet.domain.domains.pet.service.PetSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Stream;

@Mapper
@RequiredArgsConstructor
@Slf4j
public class CareMapper {
    private final CareSaveService careSaveService;
    private final CareSearchService careSearchService;
    private final CareLogSearchService careLogSearchService;
    private final CareLogSaveService careLogSaveService;

    private final PetSearchService petSearchService;

    @Transactional
    public void saveCare(CareSaveReq.CategoryDto categoryDto, CareSaveReq.CareInfoDto careInfoDto, List<CareSaveReq.AdditionalPetDto> additionalPetDtos) {
        persistAboutCare(categoryDto, careInfoDto, additionalPetDtos);
    }

    private void persistAboutCare(
            CareSaveReq.CategoryDto categoryDto,
            CareSaveReq.CareInfoDto careInfoDto,
            List<CareSaveReq.AdditionalPetDto> additionalPetDtos
    ) {
        List<CareCategory> categories = findOrCreateCategories(categoryDto, additionalPetDtos);
        careSaveService.saveCareCategories(categories);

        List<Care> cares = createCares(careInfoDto, categories);
        careSaveService.saveCares(cares);

        List<CareDate> dates = createCareDates(careInfoDto, cares);
        careSaveService.saveCareDates(dates);
    }

    private List<CareCategory> findOrCreateCategories(
            CareSaveReq.CategoryDto categoryDto,
            List<CareSaveReq.AdditionalPetDto> additionalPetDtos
    ) {
        List<CareCategory> categories = new ArrayList<>();
        List<Long> unchangedCategoryIds = new ArrayList<>();

        for (CareSaveReq.AdditionalPetDto additionalPetDto : additionalPetDtos) {
            if (additionalPetDto.categoryId() != 0L) {
                unchangedCategoryIds.add(additionalPetDto.categoryId());
                continue;
            }

            Pet pet = petSearchService.findPetById(additionalPetDto.petId());
            CareCategory category = categoryDto.toCareCategory();
            category.updatePet(pet);
            categories.add(category);
        }

        categories.addAll(careSearchService.findCareCategoriesByIds(unchangedCategoryIds));
        return categories;
    }

    private List<Care> createCares(CareSaveReq.CareInfoDto careInfoDto, List<CareCategory> categories) {
        return categories.stream()
                .map(careInfoDto::toCare)
                .toList();
    }

    private List<CareDate> createCareDates(CareSaveReq.CareInfoDto careInfoDto, List<Care> cares) {
        return cares.stream()
                .flatMap(care -> careInfoDto.toCareDateEntity().stream()
                        .peek(date -> date.updateCare(care)))
                .toList();
    }

    @Transactional(readOnly = true)
    public CareInfoRes.CareCategoryDto mapToCareCategoryDto(CareCategory careCategory, LocalDateTime now) {
        List<CareInfoRes.CareDto> careDtos = careCategory.getCares().stream()
                .flatMap(care -> mapToCareDtos(care, now))
                .sorted(Comparator.comparing(CareInfoRes.CareDto::careDate))
                .toList();

        return CareInfoRes.CareCategoryDto.of(careCategory.getId(), careCategory.getCategoryName(), careDtos);
    }

    private Stream<CareInfoRes.CareDto> mapToCareDtos(Care care, LocalDateTime now) {
        WeekType todayWeek = WeekType.fromLegacyType(now.getDayOfWeek().toString());

        return careSearchService.findCareDatesFromCareIdAndWeek(care.getId(), todayWeek).stream()
                .map(careDate -> {
                    boolean isClear = careLogSearchService.existsByCareDateIdOnLogDate(careDate.getId(), now);
                    return CareInfoRes.CareDto.of(care.getId(), careDate.getId(), care.getCareName(), careDate.getCareTime(), isClear);
                });
    }

    @Transactional
    public void updateCareCategory(Care care, CareCategory category, CareSaveReq.CategoryDto requestCategory) {
        // careCategory 갱신 여부
        // request.category().categoryId() == category.getId() -> 카테고리 변경 없음
        // request.category().categoryId() == 0L -> 카테고리 신규 생성 후 변경
        // request.category().categoryId() != category.getId() -> 다른 카테고리 이동 후, 기존 카테고리에 케어가 없을 경우 삭제
        if (requestCategory.categoryId() == 0L) {
            CareCategory newCategory = CareCategory.of(requestCategory.categoryName());
            newCategory.updatePet(category.getPet());
            careSaveService.saveCareCategory(newCategory);
            care.updateCareCategory(newCategory);
        } else if (!requestCategory.categoryId().equals(category.getId())) {
            CareCategory newCategory = careSearchService.findCareCategoryById(requestCategory.categoryId());
            care.updateCareCategory(newCategory);

            deleteCareCategoryIfEmptyCare(category);
        }
    }

    @Transactional(readOnly = true)
    public void updateCareDates(Long careId, CareSaveReq.CareInfoDto requestCare) {
        // careDates 갱신 여부
        // request.care().careDates() -> 기존 careDates와 비교하여 추가, 삭제, 수정
        // week, time이 같은 careDate가 존재하면 유지
        // 같은 week, time이 다르면 수정 후 오늘 자 로그 있으면 삭제
        // 기존 week가 없으면 추가
        Map<WeekType, Map<LocalTime, CareDate>> currentCareDatesMap = new HashMap<>();
        List<CareDate> currentCareDates = careSearchService.findCareDatesFromCareId(careId);
        for (CareDate currentCareDate : currentCareDates) {
            currentCareDatesMap.computeIfAbsent(currentCareDate.getWeek(), k -> new HashMap<>()).put(currentCareDate.getCareTime(), currentCareDate);
        }

        List<CareDate> requestCareDates = requestCare.toCareDateEntity();
        List<CareDate> newCareDates = new ArrayList<>();
        for (CareDate requestCareDate : requestCareDates) {
            Map<LocalTime, CareDate> weekTypeMap = currentCareDatesMap.getOrDefault(requestCareDate.getWeek(), new HashMap<>());
            CareDate currentCareDate = weekTypeMap.get(requestCareDate.getCareTime());

            if (currentCareDate == null) {
                newCareDates.add(requestCareDate);
            } else if (!currentCareDate.getCareTime().equals(requestCareDate.getCareTime())) {
                currentCareDate.updateCareTime(requestCareDate.getCareTime());
                if (careLogSearchService.existsByCareDateIdOnLogDate(currentCareDate.getId(), LocalDateTime.now())) {
                    CareLog careLog = careLogSearchService.findByCareDateIdOnLogDate(currentCareDate.getId(), LocalDateTime.now());
                    careLogSaveService.delete(careLog);
                }
            }

            weekTypeMap.remove(requestCareDate.getCareTime());
        }
        careSaveService.saveCareDates(newCareDates);

        List<CareDate> deleteCareDates = currentCareDatesMap.values().stream()
                .flatMap(map -> map.values().stream())
                .toList();
        careSaveService.deleteCareDates(deleteCareDates);
    }

    @Transactional
    public CareLog doCare(Long careDateId) throws GlobalErrorException {
        CareDate careDate = careSearchService.findCareDateById(careDateId);
        LocalDateTime today = LocalDateTime.now();

        validateTodayRequest(careDate.getWeek(), today);

        if (careLogSearchService.existsByCareDateIdOnLogDate(careDate.getId(), today)) {
            log.warn("이미 케어를 수행한 기록이 존재합니다.");
            throw new GlobalErrorException(CareErrorCode.ALREADY_CARED);
        }

        return careLogSaveService.save(CareLog.of(careDate));
    }

    @Transactional
    public void cancelCare(Long careDateId) throws GlobalErrorException {
        CareDate careDate = careSearchService.findCareDateById(careDateId);
        LocalDateTime today = LocalDateTime.now();

        validateTodayRequest(careDate.getWeek(), today);

        CareLog careLog = careLogSearchService.findByCareDateIdOnLogDate(careDateId, today);
        careLogSaveService.delete(careLog);
    }

    private void validateTodayRequest(WeekType weekType, LocalDateTime today) throws GlobalErrorException {
        if (!weekType.checkToday()) {
            log.warn("오늘 날짜에 대한 요청이 아닙니다. {} <- 요청 : {}", WeekType.fromLegacyType(today.getDayOfWeek().toString()), LocalDateTime.now().getDayOfWeek());
            throw new GlobalErrorException(CareErrorCode.NOT_TODAY_CARE);
        }
    }

    @Transactional
    public void deleteCare(Care care) {
        CareCategory careCategory = care.getCareCategory();
        careSaveService.deleteCare(care);
        deleteCareCategoryIfEmptyCare(careCategory);
    }

    private void deleteCareCategoryIfEmptyCare(CareCategory careCategory) {
        if (!careSearchService.existsCareUnderCategory(careCategory.getId())) {
            careSaveService.deleteCareCategory(careCategory);
        }
    }
}
