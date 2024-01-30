package com.kcy.fitapet.domain.memo.dao;

import com.kcy.fitapet.domain.memo.dto.MemoInfoDto;

import java.util.Optional;

public interface MemoQueryDslRepository {
    Optional<MemoInfoDto.MemoInfo> findMemoAndMemoImageUrlsById(Long memoId);
}
