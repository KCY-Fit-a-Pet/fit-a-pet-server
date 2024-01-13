package com.kcy.fitapet.domain.care.service.component;

import com.kcy.fitapet.domain.care.domain.Care;
import com.kcy.fitapet.domain.care.domain.CareCategory;
import com.kcy.fitapet.domain.care.domain.CareDate;
import com.kcy.fitapet.domain.care.dto.CareCategoryDto;
import com.kcy.fitapet.domain.care.dto.CareSaveReq;
import com.kcy.fitapet.domain.care.service.module.CareSaveService;
import com.kcy.fitapet.domain.care.service.module.CareSearchService;
import com.kcy.fitapet.domain.member.service.module.MemberSearchService;
import com.kcy.fitapet.domain.pet.domain.Pet;
import com.kcy.fitapet.domain.pet.exception.PetErrorCode;
import com.kcy.fitapet.domain.pet.service.module.PetSearchService;
import com.kcy.fitapet.global.common.response.exception.GlobalErrorException;
import com.kcy.fitapet.global.common.security.jwt.exception.AuthErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CareManageService {
    private final CareSaveService careSaveService;

    private final MemberSearchService memberSearchService;
    private final CareSearchService careSearchService;
    private final PetSearchService petSearchService;

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
        log.info("추가 케어 등록 요청 - 반려 동물 ID: {}, 카테고리 이름: {}", petIds, categoryDto.categoryName());
        if (!memberSearchService.isManagerAll(userId, petIds)) {
            log.warn("관리자 자격이 없는 반려 동물에 대한 케어 등록 요청");
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
        List<CareCategory> categories = new ArrayList<>(careSearchService.findAllCareCategoriesById(
                additionalPetDtos.stream().map(CareSaveReq.AdditionalPetDto::categoryId).filter(id -> !id.equals(0L)).toList()));

        for (CareSaveReq.AdditionalPetDto dto : additionalPetDtos) {
            if (dto.categoryId() != 0L) continue;

            Pet pet = petSearchService.findPetById(dto.petId());
            log.info("새로운 케어 카테고리 등록 - 반려 동물 ID: {}, 카테고리 이름: {}", pet.getId(), categoryDto.categoryName());
            CareCategory category = categoryDto.toCareCategory();
            category.updatePet(pet);
            categories.add(category);
        }
        careSaveService.saveCareCategories(categories);

        log.info("등록된 카테고리 목록: {}", categories);
        List<Care> cares = new ArrayList<>();
        for (CareCategory category : categories) {
            Care care = careInfoDto.toCare(category);
            care.updateCareCategory(category);
            cares.add(care);
        }
        careSaveService.saveCares(cares);

        List<CareDate> dates = new ArrayList<>();
        for (Care care : cares) {
            List<CareDate> requestDates = careInfoDto.toCareDateEntity();
            for (CareDate date : requestDates) date.updateCare(care);
            dates.addAll(requestDates);
        }
        careSaveService.saveCareDates(dates);
    }
}
