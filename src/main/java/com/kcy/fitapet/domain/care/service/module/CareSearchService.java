package com.kcy.fitapet.domain.care.service.module;

import com.kcy.fitapet.domain.care.dao.CareCategoryJpaRepository;
import com.kcy.fitapet.domain.care.dao.CareDateJpaRepository;
import com.kcy.fitapet.domain.care.dao.CareJpaRepository;
import com.kcy.fitapet.domain.care.domain.Care;
import com.kcy.fitapet.domain.care.domain.CareCategory;
import com.kcy.fitapet.domain.care.domain.CareDate;
import com.kcy.fitapet.domain.care.dto.CareCategoryDto;
import com.kcy.fitapet.domain.care.type.WeekType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CareSearchService {
    private final CareJpaRepository careRepository;
    private final CareCategoryJpaRepository careCategoryRepository;
    private final CareDateJpaRepository careDateRepository;

    @Transactional(readOnly = true)
    public Care findCareById(Long careId) {
        return careRepository.findByIdOrElseThrow(careId);
    }

    @Transactional(readOnly = true)
    public CareDate findCareDateById(Long careDateId) {
        return careDateRepository.findByIdOrElseThrow(careDateId);
    }

    @Transactional(readOnly = true)
    public List<CareDate> findCareDatesCareIdAndWeek(Long careId, WeekType week) {
        return careDateRepository.findAllByCare_IdAndWeek(careId, week);
    }

    @Transactional(readOnly = true)
    public CareCategory findCareCategoryById(Long categoryId) {
        return careCategoryRepository.findByIdOrElseThrow(categoryId);
    }

    @Transactional(readOnly = true)
    public List<CareCategory> findAllCareCategoriesById(List<Long> categoryIds) {
        return careCategoryRepository.findAllById(categoryIds);
    }

    @Transactional(readOnly = true)
    public List<CareCategory> findAllCareCategoriesByPetId(Long petId) {
        return careCategoryRepository.findAllByPet_Id(petId);
    }

    @Transactional(readOnly = true)
    public List<?> checkCategoryExist(String categoryName, List<Long> petIds) {
        CareCategoryDto dto = new CareCategoryDto();

        for (Long petId : petIds) {
            List<CareCategory> careCategories = careCategoryRepository.findAllByPet_Id(petId);
            for (CareCategory careCategory : careCategories) {
                if (careCategory.getCategoryName().equals(categoryName)) {
                    dto.addCareCategoryExist(petId, careCategory.getId());
                    break;
                }
            }
        }

        return dto.getCareCategoryExists();
    }
}
