package kr.co.fitapet.domain.domains.member.dto;


import kr.co.fitapet.domain.domains.care.domain.CareCategory;

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
