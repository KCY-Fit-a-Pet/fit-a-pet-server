package kr.co.fitapet.api.apis.memo.dto;

import kr.co.fitapet.common.annotation.Dto;

@Dto(name = "memoCategory")
public record MemoCategorySaveRes(
        Long memoCategoryId
) {
    public static MemoCategorySaveRes valueOf(Long memoCategoryId) {
        return new MemoCategorySaveRes(memoCategoryId);
    }
}
