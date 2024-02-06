package kr.co.fitapet.domain.domains.memo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import kr.co.fitapet.domain.common.annotation.Dto;
import lombok.Builder;
import lombok.Getter;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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
            String categorySuffix,
            String title,
            String content,
//            @JsonSerialize(using = LocalDateTimeSerializer.class)
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            LocalDateTime createdAt,
            List<MemoImageInfo> memoImages
    ) {
        public MemoInfo(Long memoId, String categorySuffix, String title, String content, LocalDateTime createdAt, List<MemoImageInfo> memoImages) {
            this.memoId = memoId;
            this.categorySuffix = categorySuffix;
            this.title = title;
            this.content = content;
            this.createdAt = createdAt;
            this.memoImages = (memoImages == null) ? List.of() : memoImages;
        }
    }

    @Builder
    @Dto(name = "memo")
    public record MemoSummaryInfo(
            Long memoId,
            String categorySuffix,
            String title,
            String content,
//            @JsonSerialize(using = LocalDateTimeSerializer.class)
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            LocalDateTime createdAt,
            MemoImageInfo memoImage
    ) {
        public MemoSummaryInfo(Long memoId, String categorySuffix, String title, String content, LocalDateTime createdAt, MemoImageInfo memoImage) {
            this.memoId = memoId;
            this.categorySuffix = categorySuffix;
            this.title = title.length() == 19 ? title + "..." : title;
            this.content = content.length() == 16 ? content.replace("\n", "  ") + "..." : content.replace("\n", "  ");
            this.createdAt = createdAt;
            this.memoImage = memoImage;
        }
    }

    public record MemoImageInfo(
            @JsonInclude(JsonInclude.Include.NON_NULL)
            Long memoImageId,
            @JsonInclude(JsonInclude.Include.NON_EMPTY)
            String imgUrl
    ) {
        public MemoImageInfo(Long memoImageId, String imgUrl) {
            this.memoImageId = memoImageId;
            this.imgUrl = Objects.toString(imgUrl, "");
        }
    }

    public record PageResponse(
//            @Schema(description = "메모 목록")
            List<?> memos,
//            @Schema(description = "현재 페이지")
            int currentPageNumber,
//            @Schema(description = "페이지 크기")
            int pageSize,
//            @Schema(description = "현재 페이지의 데이터 개수")
            int numberOfElements,
//            @Schema(description = "다음 페이지 존재 여부")
            boolean hasNext
    ) {
        public static PageResponse from(@NotNull Slice<?> page) {
            return new PageResponse(page.getContent(), page.getPageable().getPageNumber(), page.getPageable().getPageSize(), page.getNumberOfElements(), page.hasNext());
        }
    }
}
