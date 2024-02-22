package kr.co.fitapet.domain.domains.care.service;

import kr.co.fitapet.common.annotation.DomainService;
import kr.co.fitapet.domain.domains.care.domain.Care;
import kr.co.fitapet.domain.domains.care.domain.CareCategory;
import kr.co.fitapet.domain.domains.care.domain.CareDate;
import kr.co.fitapet.domain.domains.care.dto.CareCategoryInfo;
import kr.co.fitapet.domain.domains.care.repository.CareCategoryRepository;
import kr.co.fitapet.domain.domains.care.repository.CareDateRepository;
import kr.co.fitapet.domain.domains.care.repository.CareRepository;
import kr.co.fitapet.domain.domains.care.type.WeekType;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@DomainService
@RequiredArgsConstructor
public class CareSearchService {
    private final CareRepository careRepository;
    private final CareCategoryRepository careCategoryRepository;
    private final CareDateRepository careDateRepository;

    @Transactional(readOnly = true)
    public Care findCareById(Long careId) {
        return careRepository.findByIdOrElseThrow(careId);
    }

    @Transactional(readOnly = true)
    public CareDate findCareDateById(Long careDateId) {
        return careDateRepository.findByIdOrElseThrow(careDateId);
    }

    @Transactional(readOnly = true)
    public List<CareDate> findCareDatesFromCareIdAndWeek(Long careId, WeekType week) {
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
        CareCategoryInfo dto = new CareCategoryInfo();

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

    @Transactional(readOnly = true)
    public boolean existsCareUnderCategory(Long categoryId) {
        return careRepository.existsByCareCategory_Id(categoryId);
    }
}
