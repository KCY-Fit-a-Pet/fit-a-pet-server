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

        // TODO: CREATE인데 이미 존재하는 카테고리 이름을 제외할 것인가?
        CareCategory category = (categoryDto.state().equals(CareSaveDto.CategoryState.EXIST))
                ? careSearchService.findCareCategoryById(categoryDto.categoryId())
                : categoryDto.toCareCategory();
        careSaveService.saveCareCategory(category);
        log.info("category: {}", category);

        Care care = (careInfoDto.toCare(category));
        careSaveService.saveCare(care);
        log.info("care: {}", care);

        List<CareDate> dates = careInfoDto.toCareDateEntity();
        for (CareDate date : dates) date.updateCare(care);
        log.info("dates: {}", dates);

        PetCare petCare = new PetCare();
        petCare.updateMapping(petSearchService.findPetById(petId), care);
        petIds.remove(petId);

        // TODO: 존재하지 않는 반려동물 id 요청하면?
        if (memberSearchService.isManagerAll(userId, petIds)) {
            List<Pet> additionalPets = petSearchService.findPetsByIds(petIds);
            for (Pet pet : additionalPets) {
                PetCare additionalPetCare = new PetCare();
                additionalPetCare.updateMapping(pet, care);
            }
        } else {
            throw new GlobalErrorException(AuthErrorCode.FORBIDDEN_ACCESS_TOKEN);
        }
    }
}
