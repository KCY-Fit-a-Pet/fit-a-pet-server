package com.kcy.fitapet.domain.care.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.kcy.fitapet.domain.care.domain.CareCategory;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
public class CareInfoRes {
    private List<?> info = new ArrayList<>();

    public static CareInfoRes from(List<CareCategoryDto> careCategories) {
        CareInfoRes careInfoRes = new CareInfoRes();
        careInfoRes.info = careCategories;
        return careInfoRes;
    }

    public record CareCategoryDto(
        Long careCategoryId,
        String categoryName,
        List<CareDto> cares
    ) {
        public static CareCategoryDto of(Long id, String categoryName, List<CareDto> cares) {
            cares.sort(Comparator.comparing(CareDto::careDate));
            return new CareCategoryDto(id, categoryName, cares);
        }
    }

    public record CareDto(
            Long careId,
            Long careDateId,
            String careName,
            @JsonSerialize(using = LocalTimeSerializer.class)
            @JsonFormat(pattern = "HH:mm:ss")
            LocalTime careDate,
            boolean isClear
    ) {
        public static CareDto of(Long careId, Long careDateId, String careName, LocalTime careDate, boolean isClear) {
            return new CareDto(careId, careDateId, careName, careDate, isClear);
        }
    }
}
