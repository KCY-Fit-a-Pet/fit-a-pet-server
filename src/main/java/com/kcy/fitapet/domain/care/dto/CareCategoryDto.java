package com.kcy.fitapet.domain.care.dto;

import com.kcy.fitapet.domain.care.domain.CareCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class CareCategoryDto {
    private List<CareCategorySummary> careCategorySummaries = new ArrayList<>();
    private List<CareCategoryExist> careCategoryExists = new ArrayList<>();

    public static CareCategoryDto from(List<CareCategory> careCategories) {
        CareCategoryDto careCategoryDto = new CareCategoryDto();
        careCategories.forEach(careCategoryDto::addCareCategorySummary);
        return careCategoryDto;
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

    @Schema(description = "반려동물 케어 카테고리 존재 여부 확인 요청")
    public record CareCategoryExistRequest(
            @NotEmpty @Schema(description = "카테고리 이름", example = "식사")
            String categoryName,
            @NotNull @Schema(description = "반려동물 ID 목록", example = "[1, 2]")
            List<Long> pets) { }

    private record CareCategoryExist(
            Long petId,
            Long careCategoryId
    ) {

    }
}
