package kr.co.fitapet.api.apis.memo.mapper;

import kr.co.fitapet.api.apis.memo.dto.MemoPatchReq;
import kr.co.fitapet.api.common.mapstruct.JsonNullableMapper;
import kr.co.fitapet.common.annotation.Helper;
import kr.co.fitapet.common.execption.GlobalErrorException;
import kr.co.fitapet.domain.domains.memo.domain.Memo;
import kr.co.fitapet.domain.domains.memo.exception.MemoErrorCode;
import lombok.RequiredArgsConstructor;

import javax.annotation.processing.Generated;

/**
 * 추후 편의를 위해 테스트용으로 만들어본 것이므로, 실용성은 없는 Helper 클래스입니다.
 */
@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-02-12T12:21:00",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17 (AdoptOpenJDK)"
)
@Helper
@RequiredArgsConstructor
public class MemoBindingHelperImpl implements MemoBindingHelper {
    private final JsonNullableMapper jsonNullableMapper;

    @Override
    public Memo map(MemoPatchReq req) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public MemoPatchReq map(Memo memo) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void update(MemoPatchReq req, Memo destination) {
        if (req == null) return;

        if (req.title() != null) {
            if (req.title().isBlank()) throw new GlobalErrorException(MemoErrorCode.MEMO_TITLE_NOT_EMPTY);
            destination.updateTitle(req.title());
        }

        if (req.content() != null) {
            if (req.content().isBlank()) throw new GlobalErrorException(MemoErrorCode.MEMO_CONTENT_NOT_EMPTY);
            destination.updateContent(req.content());
        }
    }
}
