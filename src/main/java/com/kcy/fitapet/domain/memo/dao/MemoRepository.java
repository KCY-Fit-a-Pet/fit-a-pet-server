package com.kcy.fitapet.domain.memo.dao;

import com.kcy.fitapet.domain.memo.domain.Memo;
import com.kcy.fitapet.global.common.repository.ExtendedRepository;

public interface MemoRepository extends ExtendedRepository<Memo, Long> {
    boolean existsByIdAndMemoCategory_Id(Long memoId, Long memoCategoryId);
}
