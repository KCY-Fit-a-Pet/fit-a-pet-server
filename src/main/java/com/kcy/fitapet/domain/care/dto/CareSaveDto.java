package com.kcy.fitapet.domain.care.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.kcy.fitapet.domain.care.domain.CareCategory;
import com.kcy.fitapet.domain.care.domain.CareDate;
import com.kcy.fitapet.domain.care.type.WeekType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public class CareSaveDto {
    @Schema(description = "케어 등록 요청")
    public record Request(
            CategoryDto category,
            CareInfoDto care,
            @Schema(description = "케어 동물 추가 (등록 중인 반려동물 제외)", example = "2, 3")
            List<Long> pets
    ) {
        public CareCategory toCareCategoryEntity() {
            return CareCategory.of(category.categoryName());
        }

        public List<CareDate> toCareDateEntity() {
            return care.careDate.stream()
                    .map(careDateDto -> CareDate.of(careDateDto.week(), careDateDto.time()))
                    .collect(toMap(CareDate::getWeek, careDate -> careDate, (o1, o2) -> o1))
                    .values()
                    .stream()
                    .toList();
        }

        public List<Long> toPetIds() {
            return pets;
        }
    }

    @Schema(description = "케어 등록 - 카테고리")
    private record CategoryDto(
        @Schema(description = "카테고리 상태", example = "CREATE/EXIST")
        @NotNull
        CategoryState state,
        @Schema(description = "카테고리 ID", example = "1")
        @NotNull
        Long categoryId,
        @Schema(description = "카테고리 이름", example = "식사")
        @NotNull
        String categoryName
    ) {}

    @Schema(description = "케어 등록 - 케어 정보")
    private record CareInfoDto(
            @Schema(description = "케어 이름", example = "1")
            @NotBlank
            String careName,
            List<CareDateDto> careDate
    ) {

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

    public enum CategoryState {
        CREATE, EXIST;

        @JsonCreator
        public static CategoryState from(String s) {
            return Stream.of(CategoryState.values())
                    .filter(v -> v.name().equalsIgnoreCase(s))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(String.format("존재하지 않는 상태입니다. : %s", s)));
        }
    }
}
