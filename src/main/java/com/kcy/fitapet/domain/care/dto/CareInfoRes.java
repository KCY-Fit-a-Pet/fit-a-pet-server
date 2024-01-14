package com.kcy.fitapet.domain.care.dto;

import com.kcy.fitapet.domain.care.domain.CareCategory;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CareInfoRes {
    private List<?> info = new ArrayList<>();

    public static CareInfoRes from(List<CareCategory> careCategories) {
        CareInfoRes careInfoRes = new CareInfoRes();
        careInfoRes.info = careCategories;
        return careInfoRes;
    }

    public record CareCategoryDto(
        Long id,
        String categoryName,
        List<CareDto> cares
    ) {

    }

    public record CareDto(
            Long id,
            String careName,
            boolean isClear
    ) {

    }
}
