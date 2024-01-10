package com.kcy.fitapet.domain.member.dto.account;

import com.kcy.fitapet.domain.care.domain.CareCategory;

public record CareCategoryNamesRes(
    Long id,
    String name
) {
    public static CareCategoryNamesRes from(CareCategory careCategory) {
        return new CareCategoryNamesRes(
            careCategory.getId(),
            careCategory.getCategoryName()
        );
    }
}
