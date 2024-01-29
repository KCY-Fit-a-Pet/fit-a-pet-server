package com.kcy.fitapet.domain.memo.dao;

import com.kcy.fitapet.domain.memo.dto.MemoCategoryInfoDto;

import java.util.List;

public interface MemoCategoryQueryDslRepository {
    MemoCategoryInfoDto.MemoCategoryQueryDslRes findMemoCategoryById(Long memoCategoryId);
    List<Long> findRootMemoCategoryIdByPetId(List<Long> petId);
    List<MemoCategoryInfoDto.MemoCategoryQueryDslRes> findMemoCategoriesByParent(Long parentId);
}
