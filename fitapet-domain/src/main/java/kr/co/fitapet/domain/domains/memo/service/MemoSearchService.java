package kr.co.fitapet.domain.domains.memo.service;

import kr.co.fitapet.common.annotation.DomainService;
import kr.co.fitapet.common.execption.GlobalErrorException;
import kr.co.fitapet.domain.domains.memo.domain.MemoCategory;
import kr.co.fitapet.domain.domains.memo.dto.MemoCategoryInfoDto;
import kr.co.fitapet.domain.domains.memo.dto.MemoInfoDto;
import kr.co.fitapet.domain.domains.memo.exception.MemoErrorCode;
import kr.co.fitapet.domain.domains.memo.repository.MemoCategoryRepository;
import kr.co.fitapet.domain.domains.memo.repository.MemoImageRepository;
import kr.co.fitapet.domain.domains.memo.repository.MemoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@DomainService
@Slf4j
@RequiredArgsConstructor
public class MemoSearchService {
    private final MemoRepository memoRepository;
    private final MemoCategoryRepository memoCategoryRepository;
    private final MemoImageRepository memoImageRepository;

    @Transactional(readOnly = true)
    public MemoCategory findMemoCategoryById(Long memoCategoryId) {
        return memoCategoryRepository.findByIdOrElseThrow(memoCategoryId);
    }

    @Transactional(readOnly = true)
    public List<Long> findRootMemoCategoriesIdByPetIds(List<Long> petIds) {
        return memoCategoryRepository.findRootMemoCategoryIdByPetId(petIds);
    }

    @Transactional(readOnly = true)
    public MemoCategoryInfoDto.MemoCategoryInfo findMemoCategoryWithMemoCount(Long memoCategoryId) {
        MemoCategoryInfoDto.MemoCategoryQueryDslRes dto = memoCategoryRepository.findMemoCategoryById(memoCategoryId);
        log.info("dto: {}", dto);

        if (dto == null) throw new GlobalErrorException(MemoErrorCode.MEMO_CATEGORY_NOT_FOUND);
        if (dto.parentId() == null) {
            List<MemoCategoryInfoDto.MemoCategoryQueryDslRes> subMemoCategories = memoCategoryRepository.findMemoCategoriesByParent(dto.memoCategoryId());

            return MemoCategoryInfoDto.MemoCategoryInfo.ofRootInstance(dto, subMemoCategories);
        }

        return MemoCategoryInfoDto.MemoCategoryInfo.from(dto);
    }

    @Transactional(readOnly = true)
    public MemoInfoDto.MemoInfo findMemoAndMemoImageUrlsById(Long memoId) {
        return memoRepository.findMemoAndMemoImageUrlsById(memoId).orElseThrow(
                () -> new GlobalErrorException(MemoErrorCode.MEMO_NOT_FOUND)
        );
    }

    @Transactional(readOnly = true)
    public MemoInfoDto.PageResponse findMemosInMemoCategory(Long memoCategoryId, Pageable pageable, String target) {
        Slice<MemoInfoDto.MemoSummaryInfo> page = memoRepository.findMemosInMemoCategory(memoCategoryId, pageable, target);

        return MemoInfoDto.PageResponse.from(page);
    }

    @Transactional(readOnly = true)
    public MemoInfoDto.PageResponse findMemosByPetId(Long petId, Pageable pageable) {
        Slice<MemoInfoDto.MemoSummaryInfo> page = memoRepository.findMemosByPetId(petId, pageable);

        return MemoInfoDto.PageResponse.from(page);
    }
}