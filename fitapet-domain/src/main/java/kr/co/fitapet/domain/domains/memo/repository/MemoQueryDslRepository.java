package kr.co.fitapet.domain.domains.memo.repository;

import kr.co.fitapet.domain.domains.memo.dto.MemoInfoDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Optional;

public interface MemoQueryDslRepository {
    Optional<MemoInfoDto.MemoInfo> findMemoAndMemoImageUrlsById(Long memoId);
    Slice<MemoInfoDto.MemoSummaryInfo> findMemosInMemoCategory(Long memoCategoryId, Pageable pageable, String target);
    Slice<MemoInfoDto.MemoSummaryInfo> findMemosByPetId(Long petId, Pageable pageable);
}
