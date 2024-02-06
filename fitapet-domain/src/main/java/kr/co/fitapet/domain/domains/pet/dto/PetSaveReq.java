package kr.co.fitapet.domain.domains.pet.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.co.fitapet.domain.domains.pet.domain.Pet;
import kr.co.fitapet.domain.domains.pet.type.GenderType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.time.LocalDate;

//@Schema(description = "반려동물 등록 요청")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PetSaveReq {
//    @Schema(description = "반려동물 이름") @NotBlank(message = "반려동물 이름을 입력해주세요.")
    private String petName;
//    @Schema(description = "반려동물 종류", example = "강아지") @NotBlank(message = "반려동물 종류를 입력해주세요.")
    private String species;
//    @Schema(description = "반려동물 품종", example = "MALE") @NotNull(message = "반려동물 품종을 입력해주세요.")
    private GenderType gender;
//    @Schema(description = "중성화 여부", example = "false") @NotNull(message = "중성화 여부를 입력해주세요.")
    private boolean neutralization;
//    @Schema(description = "반려동물 생년월일", example = "yyyy-mm-dd") @NotNull(message = "생년월일을 입력해주세요.")
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul") @Past(message = "생년월일은 과거 날짜여야 합니다.")
    private LocalDate birthdate;
//    @Schema(description = "반려동물 프로필 이미지", nullable = true)
    private String profileImg;

    public Pet toPetEntity() {
        return Pet.builder()
                .petName(petName)
                .species(species)
                .gender(gender)
                .neutered(neutralization)
                .birthdate(birthdate)
                .petProfileImg(StringUtils.hasText(profileImg) ? profileImg : null)
                .build();
    }
}
