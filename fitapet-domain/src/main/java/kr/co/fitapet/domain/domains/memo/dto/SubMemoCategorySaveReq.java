package kr.co.fitapet.domain.domains.memo.dto;

import kr.co.fitapet.domain.domains.memo.domain.MemoCategory;
import kr.co.fitapet.domain.domains.pet.domain.Pet;

//@Schema(description = "서브 메모 카테고리 저장 요청")
public record SubMemoCategorySaveReq(
//        @Schema(description = "하위 메모 카테고리 이름")
//        @NotBlank(message = "하위 메모 카테고리 이름은 필수입니다.")
//        @Size(max = 20, message = "하위 메모 카테고리 이름은 20자 이하로 입력해주세요.")
        String subMemoCategoryName
) {
    public MemoCategory toEntity(MemoCategory parent, Pet pet) {
        return MemoCategory.ofChildrenInstance(subMemoCategoryName, parent, pet);
    }
}
