package com.kcy.fitapet.domain.memo.dao;

import com.kcy.fitapet.domain.memo.dto.MemoInfoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Optional;

public interface MemoQueryDslRepository {
    Optional<MemoInfoDto.MemoInfo> findMemoAndMemoImageUrlsById(Long memoId);
    Slice<MemoInfoDto.MemoInfo> findMemosInMemoCategory(Long memoCategoryId, Pageable pageable, String target);
}
