package com.kcy.fitapet.domain.care.service.component;

import com.kcy.fitapet.domain.care.domain.Care;
import com.kcy.fitapet.domain.care.domain.CareCategory;
import com.kcy.fitapet.domain.care.domain.CareDate;
import com.kcy.fitapet.domain.care.dto.CareCategoryDto;
import com.kcy.fitapet.domain.care.dto.CareSaveDto;
import com.kcy.fitapet.domain.care.service.module.CareSaveService;
import com.kcy.fitapet.domain.care.service.module.CareSearchService;
import com.kcy.fitapet.domain.member.service.module.MemberSearchService;
import com.kcy.fitapet.domain.pet.domain.Pet;
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
    public void saveCare(Long userId, Long petId, CareSaveDto.Request request) {
        CareSaveDto.CategoryDto categoryDto = request.category();
        CareSaveDto.CareInfoDto careInfoDto = request.care();
        List<Long> petIds = request.pets();

        if (!petIds.contains(petId)) petIds.add(petId);

        List<Pet> pets = petSearchService.findPetsByIds(petIds);

        if (!memberSearchService.isManagerAll(userId, petIds))
            throw new GlobalErrorException(AuthErrorCode.FORBIDDEN_ACCESS_TOKEN);

        persistAboutCare(categoryDto, careInfoDto, pets);
    }

    private void persistAboutCare(CareSaveDto.CategoryDto categoryDto, CareSaveDto.CareInfoDto careInfoDto, List<Pet> pets) {
        List<CareCategory> categories = new ArrayList<>();
        for (Pet pet : pets) {
            CareCategory category = mappingCareCategory(categoryDto, pet);
            categories.add(category);
        }
        careSaveService.saveCareCategories(categories);

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

    private CareCategory mappingCareCategory(CareSaveDto.CategoryDto categoryDto, Pet pet) {
        CareCategory category = (categoryDto.state().equals(CareSaveDto.CategoryState.EXIST))
                ? careSearchService.findCareCategoryById(categoryDto.categoryId())
                : categoryDto.toCareCategory();
        category.updatePet(pet);
        return category;
    }
}
