package kr.co.fitapet.domain.domains.care.dto;

import kr.co.fitapet.domain.domains.care.domain.CareCategory;

import java.util.ArrayList;
import java.util.List;

public class CareCategoryInfoRes {
    private List<CareCategorySummary> careCategorySummaries = new ArrayList<>();
    private List<CareCategoryExist> careCategoryExists = new ArrayList<>();

    public static CareCategoryInfoRes from(List<CareCategory> careCategories) {
        CareCategoryInfoRes careCategoryInfoRes = new CareCategoryInfoRes();
        careCategories.forEach(careCategoryInfoRes::addCareCategorySummary);
        return careCategoryInfoRes;
    }

    public void addCareCategoryExist(Long petId, Long careCategoryId) {
        careCategoryExists.add(new CareCategoryExist(petId, careCategoryId));
    }

    public List<CareCategorySummary> getCareCategorySummaries() {
        return List.copyOf(careCategorySummaries);
    }

    public List<CareCategoryExist> getCareCategoryExists() {
        return List.copyOf(careCategoryExists);
    }

    private void addCareCategorySummary(CareCategory careCategory) {
        careCategorySummaries.add(CareCategorySummary.from(careCategory));
    }

    private record CareCategorySummary(
            Long id,
            String categoryName
    ) {
        private static CareCategorySummary from(CareCategory careCategory) {
            return new CareCategorySummary(
                    careCategory.getId(),
                    careCategory.getCategoryName()
            );
        }
    }

//    @Schema(description = "반려동물 케어 카테고리 존재 여부 확인 요청")
    public record CareCategoryExistRequest(
//            @NotEmpty @Schema(description = "카테고리 이름", example = "식사")
            String categoryName,
//            @NotNull @Schema(description = "반려동물 ID 목록", example = "[1, 2]")
            List<Long> pets) { }

    private record CareCategoryExist(
            Long petId,
            Long careCategoryId
    ) {

    }
}
