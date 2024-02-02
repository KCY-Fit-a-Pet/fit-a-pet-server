package com.kcy.fitapet.domain.memo.service.module;

import com.kcy.fitapet.domain.memo.dao.MemoCategoryRepository;
import com.kcy.fitapet.domain.memo.dao.MemoImageRepository;
import com.kcy.fitapet.domain.memo.dao.MemoRepository;
import com.kcy.fitapet.domain.memo.domain.MemoCategory;
import com.kcy.fitapet.domain.memo.domain.QMemo;
import com.kcy.fitapet.domain.memo.domain.QMemoCategory;
import com.kcy.fitapet.domain.memo.domain.QMemoImage;
import com.kcy.fitapet.domain.memo.dto.MemoCategoryInfoDto;
import com.kcy.fitapet.domain.memo.dto.MemoInfoDto;
import com.kcy.fitapet.domain.memo.exception.MemoErrorCode;
import com.kcy.fitapet.global.common.repository.QueryHandler;
import com.kcy.fitapet.global.common.response.exception.GlobalErrorException;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.querydsl.core.group.GroupBy.groupBy;

@Service
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