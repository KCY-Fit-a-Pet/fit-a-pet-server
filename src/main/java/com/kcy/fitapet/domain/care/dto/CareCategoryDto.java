package com.kcy.fitapet.domain.care.dto;

import com.kcy.fitapet.domain.care.domain.CareCategory;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class CareCategoryDto {
    private List<CareCategorySummary> careCategorySummaries = new ArrayList<>();

    public static CareCategoryDto from(List<CareCategory> careCategories) {
        CareCategoryDto careCategoryDto = new CareCategoryDto();
        careCategories.forEach(careCategoryDto::addCareCategorySummary);
        return careCategoryDto;
    }

    public List<CareCategorySummary> getCareCategorySummaries() {
        List<CareCategorySummary> view = new ArrayList<>(careCategorySummaries);
        return view;
    }

    public void addCareCategorySummary(CareCategory careCategory) {
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
}
