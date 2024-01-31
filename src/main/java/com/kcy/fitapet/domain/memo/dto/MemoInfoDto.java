package com.kcy.fitapet.domain.memo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.kcy.fitapet.domain.memo.domain.Memo;
import com.kcy.fitapet.global.common.util.bind.Dto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class MemoInfoDto {
    private final List<MemoInfo> memoInfos;

    private MemoInfoDto(List<MemoInfo> memoInfos) {
        this.memoInfos = memoInfos;
    }

    public static MemoInfoDto of(List<MemoInfo> memoInfos) {
        return new MemoInfoDto(memoInfos);
    }

    @Builder
    @Dto(name = "memo")
    public record MemoInfo(
            Long memoId,
            String title,
            String content,
            @JsonSerialize(using = LocalDateTimeSerializer.class)
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            LocalDateTime createdAt,
            List<MemoImageInfo> memoImages
    ) {
        public MemoInfo(Long memoId, String title, String content, LocalDateTime createdAt, List<MemoImageInfo> memoImages) {
            this.memoId = memoId;
            this.title = title.length() == 19 ? title + "..." : title;
            this.content = content.length() == 16 ? content + "..." : content;
            this.createdAt = createdAt;
            this.memoImages = (memoImages == null) ? List.of() : memoImages;
        }

        public static MemoInfo from(Memo memo) {
            return MemoInfo.builder()
                    .memoId(memo.getId())
                    .title(memo.getTitle())
                    .content(memo.getContent())
                    .createdAt(memo.getCreatedAt())
                    .memoImages(List.of())
                    .build();
        }

        public static MemoInfo valueOf(Memo memo, List<MemoImageInfo> memoImageUrls) {
            return MemoInfo.builder()
                    .memoId(memo.getId())
                    .title(memo.getTitle())
                    .content(memo.getContent())
                    .createdAt(memo.getCreatedAt())
                    .memoImages(memoImageUrls)
                    .build();
        }
    }

    public record MemoImageInfo(
            Long memoImageId,
            String imgUrl
    ) {
        public MemoImageInfo(Long memoImageId, String imgUrl) {
            this.memoImageId = memoImageId;
            this.imgUrl = imgUrl;
        }
    }

    public record PageResponse(
            @Schema(description = "메모 목록") List<MemoInfo> memos,
            @Schema(description = "현재 페이지") int number,
            @Schema(description = "페이지 크기") int size,
            @Schema(description = "현재 페이지의 데이터 개수") int numberOfElements,
            @Schema(description = "다음 페이지 존재 여부") boolean hasNext
    ) {
        public static PageResponse from(@NotNull Slice<MemoInfo> page) {
            return new PageResponse(page.getContent(), page.getPageable().getPageNumber(), page.getPageable().getPageSize(), page.getNumberOfElements(), page.hasNext());
        }
    }
}
