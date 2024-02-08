package kr.co.fitapet.api.apis.memo.usecase;

import kr.co.fitapet.common.annotation.UseCase;
import kr.co.fitapet.domain.domains.memo.domain.Memo;
import kr.co.fitapet.domain.domains.memo.domain.MemoCategory;
import kr.co.fitapet.domain.domains.memo.domain.MemoImage;
import kr.co.fitapet.domain.domains.memo.dto.MemoCategoryInfoDto;
import kr.co.fitapet.domain.domains.memo.dto.MemoInfoDto;
import kr.co.fitapet.domain.domains.memo.dto.MemoSaveReq;
import kr.co.fitapet.api.apis.memo.dto.SubMemoCategorySaveReq;
import kr.co.fitapet.domain.domains.memo.service.MemoSaveService;
import kr.co.fitapet.domain.domains.memo.service.MemoSearchService;
import kr.co.fitapet.domain.domains.pet.service.PetSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@UseCase
@RequiredArgsConstructor
public class MemoUseCase {
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
