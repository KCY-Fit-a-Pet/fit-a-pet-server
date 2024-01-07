package com.kcy.fitapet.domain.care.service.component;

import com.kcy.fitapet.domain.care.domain.Care;
import com.kcy.fitapet.domain.care.domain.CareCategory;
import com.kcy.fitapet.domain.care.domain.CareDate;
import com.kcy.fitapet.domain.care.dto.CareSaveDto;
import com.kcy.fitapet.domain.care.service.module.CareSaveService;
import com.kcy.fitapet.domain.care.service.module.CareSearchService;
import com.kcy.fitapet.domain.member.service.module.MemberSearchService;
import com.kcy.fitapet.domain.pet.domain.Pet;
import com.kcy.fitapet.domain.pet.domain.PetCare;
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
    public void saveCare(Long userId, Long petId, CareSaveDto.Request request) {
        CareSaveDto.CategoryDto categoryDto = request.category();
        CareSaveDto.CareInfoDto careInfoDto = request.care();
        List<Long> petIds = request.pets();

        persistAboutCare(categoryDto, careInfoDto, petSearchService.findPetById(petId));
        petIds.remove(petId);

        if (memberSearchService.isManagerAll(userId, petIds)) {
            List<Pet> pets = petSearchService.findPetsByIds(petIds);
            for (Pet pet : pets) {
                persistAboutCare(categoryDto, careInfoDto, pet);
            }
        } else {
            throw new GlobalErrorException(AuthErrorCode.FORBIDDEN_ACCESS_TOKEN);
        }
    }

    private void persistAboutCare(CareSaveDto.CategoryDto categoryDto, CareSaveDto.CareInfoDto careInfoDto, Pet pet) {
        CareCategory category = saveCareCategory(categoryDto);

        Care care = careInfoDto.toCare(category);
        careSaveService.saveCare(care);

        List<CareDate> dates = careInfoDto.toCareDateEntity();
        for (CareDate date : dates) date.updateCare(care);

        PetCare petCare = new PetCare();
        petCare.updateMapping(pet, care);
    }

    private CareCategory saveCareCategory(CareSaveDto.CategoryDto categoryDto) {
        CareCategory category = (categoryDto.state().equals(CareSaveDto.CategoryState.EXIST))
                ? careSearchService.findCareCategoryById(categoryDto.categoryId())
                : categoryDto.toCareCategory();
        return careSaveService.saveCareCategory(category);
    }
}
