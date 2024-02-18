package kr.co.fitapet.api.apis.memo.dto;

import kr.co.fitapet.common.annotation.Dto;

@Dto(name = "memo")
public record MemoSaveRes(
        Long memoId
) {
    public static MemoSaveRes valueOf(Long memoId) {
        return new MemoSaveRes(memoId);
    }
}
