package com.kcy.fitapet.domain.memo.dao;

import com.kcy.fitapet.domain.memo.domain.MemoCategory;
import com.kcy.fitapet.global.common.repository.ExtendedRepository;

public interface MemoCategoryRepository extends ExtendedRepository<MemoCategory, Long> {
    boolean existsByIdAndPet_Id(Long memoCategoryId, Long petId);
    boolean existsByIdAndPet_IdAndParentIsNull(Long memoCategoryId, Long petId);
    boolean existsByIdAndPet_IdAndParentIsNotNull(Long memoCategoryId, Long petId);

}
