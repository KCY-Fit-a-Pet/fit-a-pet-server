package com.kcy.fitapet.domain.member.dto.account;

import com.kcy.fitapet.domain.care.domain.CareCategory;

import java.util.List;

public record CareCategoryNamesRes(
    List<String> careCategoryNames
) {
    public static CareCategoryNamesRes of(List<CareCategory> careCategories) {
        return new CareCategoryNamesRes(
            careCategories.stream()
                .map(CareCategory::getCategoryName)
                .toList()
        );
    }
}
