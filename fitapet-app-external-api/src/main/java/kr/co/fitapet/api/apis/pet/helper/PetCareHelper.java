package kr.co.fitapet.api.apis.pet.helper;

import kr.co.fitapet.common.annotation.Helper;
import kr.co.fitapet.domain.domains.care.domain.CareCategory;
import kr.co.fitapet.api.apis.care.dto.CareCategoryInfo;
import kr.co.fitapet.domain.domains.care.service.CareSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Helper
@RequiredArgsConstructor
public class PetCareHelper {
    private final CareSearchService careSearchService;

    @Transactional(readOnly = true)
    public List<?> checkCategoryExist(String categoryName, List<Long> petIds) {
        CareCategoryInfo dto = new CareCategoryInfo();

        for (Long petId : petIds) {
            List<CareCategory> careCategories = careSearchService.findCareCategoriesByPetId(petId);
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
