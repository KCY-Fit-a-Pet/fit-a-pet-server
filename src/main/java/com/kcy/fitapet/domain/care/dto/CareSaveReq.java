package com.kcy.fitapet.domain.care.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.kcy.fitapet.domain.care.domain.Care;
import com.kcy.fitapet.domain.care.domain.CareCategory;
import com.kcy.fitapet.domain.care.domain.CareDate;
import com.kcy.fitapet.domain.care.exception.CareErrorCode;
import com.kcy.fitapet.domain.care.type.WeekType;
import com.kcy.fitapet.global.common.response.exception.GlobalErrorException;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public class CareSaveReq {
    @Schema(description = "케어 등록 요청")
    public record Request(
            @NotNull
            CategoryDto category,
            @NotNull
            CareInfoDto care,
            @NotNull
            List<AdditionalPetDto> pets
    ) {
    }

    @Schema(description = "케어 등록 - 카테고리")
    public record CategoryDto(
        @Schema(description = "카테고리 ID", example = "1")
        @NotNull
        Long categoryId,
        @Schema(description = "카테고리 이름", example = "식사")
        @NotNull
        String categoryName
    ) {
        public CareCategory toCareCategory() {
            return CareCategory.of(categoryName);
        }
    }

    @Schema(description = "케어 등록 - 케어 정보")
    public record CareInfoDto(
            @Schema(description = "케어 이름", example = "1")
            @NotBlank
            String careName,
            @NotNull
            List<CareDateDto> careDate,
            @Schema(description = "제한 시간(분 단위) - 제한 시간 없는 경우 0", example = "30")
            @NotNull
            Integer limitTime
    ) {
        public Care toCare(CareCategory category) {
            return Care.of(careName, limitTime, category);
        }

        public List<CareDate> toCareDateEntity() {
            return careDate.stream()
                    .map(careDateDto -> CareDate.of(careDateDto.week(), careDateDto.time()))
                    .collect(toMap(CareDate::getWeek, careDate -> careDate, (o1, o2) -> o1))
                    .values()
                    .stream()
                    .toList();
        }
    }

    @Schema(description = "케어 등록 - 케어 날짜")
    private record CareDateDto(
        @Schema(description = "요일", example = "mon")
        @NotNull
        WeekType week,
        @Schema(description = "시간", example = "12:00:00")
        @JsonSerialize(using = LocalTimeSerializer.class)
        @JsonFormat(pattern = "HH:mm:ss")
        @NotNull
        LocalTime time
    ) {
    }

    @Schema(description = "케어 동물 추가 - 기존 카테고리에 묶을 거면 categoryId, 새로 만들 거면 0")
    public record AdditionalPetDto(
            @Schema(description = "반려동물 ID", example = "1")
            @NotNull
            Long petId,
            @Schema(description = "기존 카테고리 ID", example = "1")
            @NotNull
            Long categoryId
    ) {
        public static AdditionalPetDto of(Long petId, Long categoryId) {
            return new AdditionalPetDto(petId, categoryId);
        }
    }
}
