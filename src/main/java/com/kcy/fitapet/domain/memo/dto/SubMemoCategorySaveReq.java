package com.kcy.fitapet.domain.memo.dto;

import com.kcy.fitapet.domain.memo.domain.MemoCategory;
import com.kcy.fitapet.domain.pet.domain.Pet;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "서브 메모 카테고리 저장 요청")
public record SubMemoCategorySaveReq(
        @Schema(description = "하위 메모 카테고리 이름")
        @NotBlank(message = "하위 메모 카테고리 이름은 필수입니다.")
        @Size(max = 20, message = "하위 메모 카테고리 이름은 20자 이하로 입력해주세요.")
        String subMemoCategoryName
) {
    public MemoCategory toEntity(MemoCategory parent, Pet pet) {
        return MemoCategory.ofChildrenInstance(subMemoCategoryName, parent, pet);
    }
}
