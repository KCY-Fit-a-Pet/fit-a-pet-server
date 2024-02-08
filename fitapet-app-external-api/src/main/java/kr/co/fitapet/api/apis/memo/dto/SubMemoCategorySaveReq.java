package kr.co.fitapet.api.apis.memo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import kr.co.fitapet.domain.domains.memo.domain.MemoCategory;
import kr.co.fitapet.domain.domains.pet.domain.Pet;

@Schema(description = "서브 메모 카테고리 저장 요청")
public record SubMemoCategorySaveReq(
        @Schema(description = "하위 메모 카테고리 이름")
        @NotBlank(message = "하위 메모 카테고리 이름은 필수입니다.")
        @Size(max = 21, message = "하위 메모 카테고리 이름은 21자 이하로 입력해주세요.")
        String subMemoCategoryName
) {
    public MemoCategory toEntity(MemoCategory parent, Pet pet) {
        return MemoCategory.ofChildrenInstance(subMemoCategoryName, parent, pet);
    }
}
