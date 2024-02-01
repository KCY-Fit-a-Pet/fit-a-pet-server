package com.kcy.fitapet.domain.memo.service.component;

import com.kcy.fitapet.domain.memo.domain.Memo;
import com.kcy.fitapet.domain.memo.domain.MemoCategory;
import com.kcy.fitapet.domain.memo.domain.MemoImage;
import com.kcy.fitapet.domain.memo.dto.MemoCategoryInfoDto;
import com.kcy.fitapet.domain.memo.dto.MemoInfoDto;
import com.kcy.fitapet.domain.memo.dto.MemoSaveReq;
import com.kcy.fitapet.domain.memo.dto.SubMemoCategorySaveReq;
import com.kcy.fitapet.domain.memo.exception.MemoErrorCode;
import com.kcy.fitapet.domain.memo.service.module.MemoSaveService;
import com.kcy.fitapet.domain.memo.service.module.MemoSearchService;
import com.kcy.fitapet.domain.pet.service.module.PetSearchService;
import com.kcy.fitapet.global.common.response.exception.GlobalErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Transactional
    public void saveMemo(Long MemoCategoryId, MemoSaveReq req) {
        MemoCategory memoCategory = memoSearchService.findMemoCategoryById(MemoCategoryId);

        Memo memo = req.toEntity();
        memo.updateMemoCategory(memoCategory);

        if (req.memoImageUrls() != null) {
            memoSaveService.saveMemo(memo);
            req.memoImageUrls().forEach(url -> MemoImage.of(url, memo));
        }
    }

    @Transactional(readOnly = true)
    public MemoCategoryInfoDto.MemoCategoryInfo findCategoryById(Long memoCategoryId) {
        return memoSearchService.findMemoCategoryWithMemoCount(memoCategoryId);
    }

    @Transactional(readOnly = true)
    public MemoInfoDto.MemoInfo findMemoAndMemoImageUrlsById(Long memoId) {
        return memoSearchService.findMemoAndMemoImageUrlsById(memoId);
    }

    @Transactional(readOnly = true)
    public MemoInfoDto.PageResponse findMemosInMemoCategory(Long memoCategoryId, Pageable pageable, String target) {
        return memoSearchService.findMemosInMemoCategory(memoCategoryId, pageable, target);
    }

    @Transactional(readOnly = true)
    public MemoInfoDto.PageResponse findMemosByPetId(Long petId, Pageable pageable) {
        return memoSearchService.findMemosByPetId(petId, pageable);
    }
}
