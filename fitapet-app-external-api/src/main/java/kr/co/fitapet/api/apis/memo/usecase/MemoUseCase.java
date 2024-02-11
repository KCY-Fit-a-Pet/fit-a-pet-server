package kr.co.fitapet.api.apis.memo.usecase;

import kr.co.fitapet.api.apis.memo.dto.MemoPatchReq;
import kr.co.fitapet.api.apis.memo.mapper.MemoBindingHelper;
import kr.co.fitapet.common.annotation.UseCase;
import kr.co.fitapet.common.execption.GlobalErrorException;
import kr.co.fitapet.domain.domains.memo.domain.Memo;
import kr.co.fitapet.domain.domains.memo.domain.MemoCategory;
import kr.co.fitapet.domain.domains.memo.domain.MemoImage;
import kr.co.fitapet.domain.domains.memo.dto.MemoCategoryInfoDto;
import kr.co.fitapet.domain.domains.memo.dto.MemoInfoDto;
import kr.co.fitapet.domain.domains.memo.dto.MemoSaveReq;
import kr.co.fitapet.api.apis.memo.dto.SubMemoCategorySaveReq;
import kr.co.fitapet.domain.domains.memo.exception.MemoErrorCode;
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

    private final MemoBindingHelper memoBindingHelper;

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

    @Transactional
    public void deleteMemo(Long memoId) {
        Memo memo = memoSearchService.findMemoById(memoId);
        List<MemoImage> memoImages = memoSearchService.findMemoImagesByMemoId(memoId);

        ncpObjectStorageService.deleteObjects(memoImages.stream().map(MemoImage::getImgUrl).toList());
        memoDeleteService.deleteMemoAndMemoImages(memo, memoImages);
    }

    @Transactional
    public void patchMemo(Long memoId, MemoPatchReq req) {
        Memo memo = memoSearchService.findMemoById(memoId);

        if (req.title() != null) {
            if (req.title().isBlank()) throw new GlobalErrorException(MemoErrorCode.MEMO_TITLE_NOT_EMPTY);
            memo.updateTitle(req.title());
        }

        if (req.content() != null) {
            if (req.content().isBlank()) throw new GlobalErrorException(MemoErrorCode.MEMO_CONTENT_NOT_EMPTY);
            memo.updateContent(req.content());
        }

        if (req.memoImageUrls() != null) {
            List<String> memoImages = memoSearchService.findMemoImagesByMemoId(memoId).stream().map(MemoImage::getImgUrl).toList();

            List<String> addedMemoImageUrls = memoImages.stream().filter(imgUrl -> !req.memoImageUrls().contains(imgUrl)).toList();
            log.info("addedMemoImageUrls: {}", addedMemoImageUrls);

            List<String> deletedMemoImageUrls = req.memoImageUrls().stream().filter(imgUrl -> !memoImages.contains(imgUrl)).toList();
            log.info("deletedMemoImageUrls: {}", deletedMemoImageUrls);

        }
    }
}
