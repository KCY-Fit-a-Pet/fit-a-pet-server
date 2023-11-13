package com.kcy.fitapet.domain.pet.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kcy.fitapet.domain.care.domain.Care;
import com.kcy.fitapet.domain.pet.domain.Pet;
import com.kcy.fitapet.domain.pet.type.CareNameType;
import com.kcy.fitapet.domain.pet.type.GenderType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

@Schema(description = "반려동물 등록 요청")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PetRegisterReq {
    @Schema(description = "반려동물 이름") @NotBlank(message = "반려동물 이름을 입력해주세요.")
    private String petName;
    @Schema(description = "반려동물 종류", example = "강아지") @NotBlank(message = "반려동물 종류를 입력해주세요.")
    private String species;
    @Schema(description = "반려동물 품종", example = "MALE") @NotNull(message = "반려동물 품종을 입력해주세요.")
    private GenderType gender;
    @Schema(description = "중성화 여부", example = "false") @NotNull(message = "중성화 여부를 입력해주세요.")
    private boolean neutralization;
    @Schema(description = "반려동물 생년월일", example = "yyyy-mm-dd") @NotNull(message = "생년월일을 입력해주세요.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul") @Past(message = "생년월일은 과거 날짜여야 합니다.")
    private LocalDate birthDate;

    @Schema(description = "반려동물 프로필 이미지", nullable = true)
    private String profileImg;

    @Schema(description = "초기 케어 리스트", nullable = true, example = "['MEAL', 'WALK', 'CLEAN', 'MEDICINE', 'SNACK']") @Valid
    private List<CareName> cares;

    @Getter
    public static class CareName {
        @NotBlank(message = "유효하지 않은 케어 이름입니다.")
        private String careName;
    }

    public Pet toPetEntity() {
        return Pet.builder()
                .petName(petName)
                .species(species)
                .gender(gender)
                .neutered(neutralization)
                .birthDate(birthDate)
                .petProfileImg(profileImg)
                .build();
    }

    public List<Care> toCareEntity() {
        return cares.stream()
                .map(careName -> Care.builder()
                        .careName(careName.getCareName())
                        .dtype(CareNameType.valueOf(careName.getCareName()).getCareType())
                        .build())
                .toList();
    }
}
