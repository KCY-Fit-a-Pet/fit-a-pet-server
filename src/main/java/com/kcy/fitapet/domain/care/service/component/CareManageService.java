package com.kcy.fitapet.domain.care.service.component;

import com.kcy.fitapet.domain.care.domain.CareCategory;
import com.kcy.fitapet.domain.care.domain.CareDate;
import com.kcy.fitapet.domain.care.dto.CareSaveDto;
import com.kcy.fitapet.domain.care.service.module.CareSaveService;
import com.kcy.fitapet.domain.care.service.module.CareSearchService;
import com.kcy.fitapet.domain.pet.domain.Pet;
import com.kcy.fitapet.domain.pet.service.module.PetSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CareManageService {
    private final CareSaveService careSaveService;

    private final CareSearchService careSearchService;
    private final PetSearchService petSearchService;

    @Transactional
    public void saveCare(Long userId, CareSaveDto.Request request) {
        CareSaveDto.CategoryDto categoryDto = request.category();
        CareSaveDto.CareInfoDto careInfoDto = request.care();
        List<Long> petIds = request.pets();

        CareCategory category = (categoryDto.state().equals(CareSaveDto.CategoryState.EXIST))
                ? careSearchService.findCareCategoryById(categoryDto.categoryId())
                : categoryDto.toCareCategory();
        careSaveService.saveCareCategory(category);

        List<CareDate> dates = careInfoDto.toCareDateEntity();


    }
}
