package kr.co.fitapet.domain.domains.memo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kr.co.fitapet.domain.domains.memo.domain.Memo;

import java.util.List;

@Schema(description = "메모 저장 요청")
public record MemoSaveReq(
        @NotBlank(message = "제목은 필수입니다.")
        @Schema(description = "제목", example = "오늘의 일기", requiredMode = Schema.RequiredMode.REQUIRED)
        @Size(max = 21, message = "제목은 21자 이하로 입력해주세요.")
        String title,
        @NotBlank(message = "내용은 필수입니다.")
        @Schema(description = "내용", example = "오늘은 매우 행복했다.", requiredMode = Schema.RequiredMode.REQUIRED)
        String content,
        @NotNull(message = "메모 이미지는 필수입니다. 없으면 빈 배열을 보내주세요.")
        @Schema(description = "메모 이미지", example = "[\"https://fitapet.com/image/1.jpg\", \"https://fitapet.com/image/2.jpg\"]", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        List<String> memoImageUrls
) {
    public Memo toEntity() {
        return Memo.of(title, content);
    }
}
