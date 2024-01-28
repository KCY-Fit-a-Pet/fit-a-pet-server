package com.kcy.fitapet.domain.memo.service.component;

import com.kcy.fitapet.domain.memo.domain.MemoCategory;
import com.kcy.fitapet.domain.memo.dto.MemoCategoryInfoDto;
import com.kcy.fitapet.domain.memo.dto.SubMemoCategorySaveReq;
import com.kcy.fitapet.domain.memo.service.module.MemoSaveService;
import com.kcy.fitapet.domain.memo.service.module.MemoSearchService;
import com.kcy.fitapet.domain.pet.service.module.PetSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemoManageService {
    private final PetSearchService petSearchService;

    private final MemoSearchService memoSearchService;
    private final MemoSaveService memoSaveService;

    @Transactional
    public void saveSubMemoCategory(Long petId, Long rootMemoCategoryId, SubMemoCategorySaveReq req) {
        req.toEntity(memoSearchService.findMemoCategoryById(rootMemoCategoryId), petSearchService.findPetById(petId));
    }

    @Transactional(readOnly = true)
    public MemoCategoryInfoDto.MemoCategoryInfo findCategoryById(Long memoCategoryId) {
        return memoSearchService.findMemoCategoryWithMemoCount(memoCategoryId);
    }
}
