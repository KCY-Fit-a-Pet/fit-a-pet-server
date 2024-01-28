package com.kcy.fitapet.domain.memo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kcy.fitapet.domain.memo.domain.MemoCategory;
import com.kcy.fitapet.domain.memo.type.MemoCategoryType;
import com.kcy.fitapet.global.common.util.bind.Dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MemoCategoryInfoDto {
    private final List<?> memoCategories;

    private MemoCategoryInfoDto(List<?> memoCategories) {
        this.memoCategories = memoCategories;
    }

    public static MemoCategoryInfoDto fromMemoCategory(List<MemoCategory> memoCategories) {
        return new MemoCategoryInfoDto(memoCategories);
    }

    @Schema(description = "메모 카테고리 정보")
    @Builder
    @Dto(name = "memoCategory")
    public record MemoCategoryInfo(
        @Schema(description = "메모 카테고리 ID")
        Long memoCategoryId,
        @Schema(description = "메모 카테고리 이름")
        String memoCategoryName,
        @Schema(description = "메모 카테고리 타입 (root: 상위 카테고리, sub: 하위 카테고리)")
        MemoCategoryType type,
        @Schema(description = "메모 카테고리에 속한 메모 개수")
        Long totalMemoCount,
        @Schema(description = "하위 메모 카테고리 리스트. 메모 카테고리 타입이 root일 경우에만 존재합니다.")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        List<MemoCategoryInfo> subMemoCategories
    ) {
        /**
         * 단일 MemoCategoryQueryDslRes 객체를 MemoCategoryInfo 객체로 변환합니다. <br/>
         * SubMemoCategorySaveReq 객체를 생성할 때 사용합니다.
         */
        public static MemoCategoryInfo from(MemoCategoryQueryDslRes res) {
            return MemoCategoryInfo.builder()
                    .memoCategoryId(res.memoCategoryId())
                    .memoCategoryName(res.memoCategoryName())
                    .type(res.parentId() == null ? MemoCategoryType.ROOT : MemoCategoryType.SUB)
                    .subMemoCategories(res.parentId() == null ? new ArrayList<>() : null)
                    .totalMemoCount(res.totalMemoCount())
                    .build();
        }

        /**
         * Root MemoCategoryQueryDslRes 객체와 Sub MemoCategoryQueryDslRes 객체 리스트를 MemoCategoryInfo 객체로 변환합니다. <br/>
         */
        public static MemoCategoryInfo ofRootInstance(MemoCategoryQueryDslRes rootMemoCategory, List<MemoCategoryQueryDslRes> subMemoCategories) {
            return MemoCategoryInfo.builder()
                    .memoCategoryId(rootMemoCategory.memoCategoryId())
                    .memoCategoryName(rootMemoCategory.memoCategoryName())
                    .type(MemoCategoryType.ROOT)
                    .totalMemoCount(rootMemoCategory.totalMemoCount() + subMemoCategories.stream().mapToLong(MemoCategoryQueryDslRes::totalMemoCount).sum())
                    .subMemoCategories(subMemoCategories.stream().map(MemoCategoryInfo::from).toList())
                    .build();
        }
    }

    public record MemoCategoryQueryDslRes(
            Long memoCategoryId,
            String memoCategoryName,
            Long parentId,
            Long totalMemoCount
    ) {
        public MemoCategoryQueryDslRes(Long memoCategoryId, String memoCategoryName, Long parentId, Long totalMemoCount) {
            this.memoCategoryId = memoCategoryId;
            this.memoCategoryName = memoCategoryName;
            this.parentId = parentId;
            this.totalMemoCount = totalMemoCount;
        }
    }
}
