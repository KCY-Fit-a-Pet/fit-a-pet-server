package com.kcy.fitapet.domain.member.dto.account;

import com.kcy.fitapet.domain.care.domain.CareCategory;
import com.kcy.fitapet.global.common.util.bind.Dto;
import lombok.Getter;

import java.util.List;

@Dto(name = "careCategory")
public record CareCategoryNamesRes(
    List<String> careCategoryNames
) {
    public static CareCategoryNamesRes of(List<CareCategory> careCategories) {
        return new CareCategoryNamesRes(
                careCategories.stream()
                        .map(CareCategory::getCategoryName)
                        .distinct()
                        .sorted()
                        .toList()
        );
    }
}
