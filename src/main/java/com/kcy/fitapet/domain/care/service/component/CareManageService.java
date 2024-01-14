package com.kcy.fitapet.domain.care.service.component;

import com.kcy.fitapet.domain.care.domain.Care;
import com.kcy.fitapet.domain.care.domain.CareCategory;
import com.kcy.fitapet.domain.care.domain.CareDate;
import com.kcy.fitapet.domain.care.dto.CareCategoryDto;
import com.kcy.fitapet.domain.care.dto.CareInfoRes;
import com.kcy.fitapet.domain.care.dto.CareSaveReq;
import com.kcy.fitapet.domain.care.service.module.CareSaveService;
import com.kcy.fitapet.domain.care.service.module.CareSearchService;
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

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CareManageService {
    private final CareSaveService careSaveService;

    private final MemberSearchService memberSearchService;
    private final PetSearchService petSearchService;
    private final CareSearchService careSearchService;
    private final CareLogSearchService careLogSearchService;

    @Transactional
    public List<?> findCareCategoryNamesByPetId(Long petId) {
         List<CareCategory> careCategories = careSearchService.findAllCareCategoriesByPetId(petId);
         return CareCategoryDto.from(careCategories).getCareCategorySummaries();
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

    @Transactional(readOnly = true)
    public CareInfoRes findCaresByPetId(Long petId) {
        List<CareCategory> careCategories = careSearchService.findAllCareCategoriesByPetId(petId);
        for (CareCategory careCategory : careCategories) {
            log.info("careCategory: {}", careCategory);
            List<Care> cares = careCategory.getCares();

            for (Care care : cares) {
                log.info("care: {}", care);
//                String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                LocalDate today = LocalDate.now();
                log.info("today: {}", today);

                boolean isClear = careLogSearchService.existsByCareIdOnToday(care.getId(), today);
                log.info("isClear: {}", isClear);
            }
        }
        return null;
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
