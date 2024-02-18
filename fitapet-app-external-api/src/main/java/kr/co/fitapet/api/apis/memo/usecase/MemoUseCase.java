package kr.co.fitapet.api.apis.memo.usecase;

import kr.co.fitapet.api.apis.memo.dto.*;
import kr.co.fitapet.common.annotation.UseCase;
import kr.co.fitapet.domain.domains.memo.domain.Memo;
import kr.co.fitapet.domain.domains.memo.domain.MemoCategory;
import kr.co.fitapet.domain.domains.memo.domain.MemoImage;
import kr.co.fitapet.domain.domains.memo.dto.MemoCategoryInfoDto;
import kr.co.fitapet.domain.domains.memo.dto.MemoInfoDto;
import kr.co.fitapet.domain.domains.memo.service.MemoDeleteService;
import kr.co.fitapet.domain.domains.memo.service.MemoSaveService;
import kr.co.fitapet.domain.domains.memo.service.MemoSearchService;
import kr.co.fitapet.domain.domains.pet.service.PetSearchService;
import kr.co.fitapet.infra.client.s3.NcpObjectStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@UseCase
@RequiredArgsConstructor
public class MemoUseCase {
    private final PetSearchService petSearchService;

    private final MemoSearchService memoSearchService;
    private final MemoSaveService memoSaveService;
    private final MemoDeleteService memoDeleteService;

    private final NcpObjectStorageService ncpObjectStorageService;

    @Transactional
    public MemoCategorySaveRes saveSubMemoCategory(Long petId, Long rootMemoCategoryId, SubMemoCategorySaveReq req) {
        MemoCategory memoCategory = req.toEntity(memoSearchService.findMemoCategoryById(rootMemoCategoryId), petSearchService.findPetById(petId));
        return MemoCategorySaveRes.valueOf(memoSaveService.saveMemoCategory(memoCategory).getId());
    }

    @Transactional
    public MemoSaveRes saveMemo(Long MemoCategoryId, MemoSaveReq req) {
        MemoCategory memoCategory = memoSearchService.findMemoCategoryById(MemoCategoryId);

        Memo memo = req.toEntity();
        memo.updateMemoCategory(memoCategory);
        memoSaveService.saveMemo(memo);

        if (req.memoImageUrls() != null)
            req.memoImageUrls().forEach(url -> MemoImage.of(url, memo));

        return MemoSaveRes.valueOf(memo.getId());
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

    @Transactional
    public void deleteMemo(Long memoId) {
        Memo memo = memoSearchService.findMemoById(memoId);
        List<MemoImage> memoImages = memoSearchService.findMemoImagesByMemoId(memoId);

        memoDeleteService.deleteMemoAndMemoImages(memo, memoImages);
        ncpObjectStorageService.deleteObjects(memoImages.stream().map(MemoImage::getImgUrl).toList());
    }

    @Transactional
    public void updateMemo(Long memoId, MemoUpdateReq req) {
        Memo memo = memoSearchService.findMemoById(memoId);
        memo.updateMemo(req.toEntity());

        if (!memo.getMemoCategory().getId().equals(req.memoCategoryId())) {
            MemoCategory memoCategory = memoSearchService.findMemoCategoryById(req.memoCategoryId());
            memo.updateMemoCategory(memoCategory);
            log.info("카테고리 이동 {} -> {}", memo.getMemoCategory().getId(), req.memoCategoryId());
        }

        List<MemoImage> originalMemoImages = memoSearchService.findMemoImagesByMemoId(memoId);
        List<MemoImage> addedMemoImages = req.memoImageUrls().stream().filter(url -> originalMemoImages.stream().noneMatch(memoImage -> memoImage.getImgUrl().equals(url))).map(url -> MemoImage.of(url, memo)).toList();
        List<MemoImage> removedMemoImages = originalMemoImages.stream().filter(memoImage -> !req.memoImageUrls().contains(memoImage.getImgUrl())).toList();
        log.info("addedMemoImages: {}, removedMemoImages: {}", addedMemoImages, removedMemoImages);

        if (!removedMemoImages.isEmpty()) {
            memoDeleteService.deleteMemoImages(removedMemoImages);
            ncpObjectStorageService.deleteObjects(removedMemoImages.stream().map(MemoImage::getImgUrl).toList());
        }
    }
}
