package com.kcy.fitapet.domain.memo.dto;

import com.kcy.fitapet.domain.memo.domain.Memo;
import com.kcy.fitapet.global.common.util.bind.Dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

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
            List<String> memoImageUrls
    ) {
        public MemoInfo(Long memoId, String title, String content, List<String> memoImageUrls) {
            this.memoId = memoId;
            this.title = title;
            this.content = content;
            this.memoImageUrls = (memoImageUrls == null) ? List.of() : List.copyOf(memoImageUrls);
        }

        public static MemoInfo from(Memo memo) {
            return MemoInfo.builder()
                    .memoId(memo.getId())
                    .title(memo.getTitle())
                    .content(memo.getContent())
                    .memoImageUrls(List.of())
                    .build();
        }

        public static MemoInfo valueOf(Memo memo, List<String> memoImageUrls) {
            return MemoInfo.builder()
                    .memoId(memo.getId())
                    .title(memo.getTitle())
                    .content(memo.getContent())
                    .memoImageUrls(memoImageUrls)
                    .build();
        }
    }
}
