package kr.co.fitapet.api.apis.memo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.List;

@Schema(description = "메모 수정 요청")
public record MemoPatchReq(
        @Schema(description = "메모 제목")
        @Size(max = 21, message = "메모 제목은 21자 이하로 입력해주세요.")
        String title,
        @Schema(description = "메모 내용", example = "메모 내용")
        String content,
        @Schema(description = "기존 memoImage 리스트에서 삭제할 요소.", example = "[1, 2, 3]")
        List<Long> removedMemoImageIds,
        @Schema(description = "추가할 메모 이미지 URL 리스트", example = "['https://{bucket-name}.{base-url}/{object-name}']")
        List<String> addedMemoImageUrls
) {
}
