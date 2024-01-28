package com.kcy.fitapet.domain.memo.service.module;

import com.kcy.fitapet.domain.memo.dao.MemoCategoryRepository;
import com.kcy.fitapet.domain.memo.dao.MemoImageRepository;
import com.kcy.fitapet.domain.memo.dao.MemoRepository;
import com.kcy.fitapet.domain.memo.domain.MemoCategory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemoSaveService {
    private final MemoRepository memoRepository;
    private final MemoCategoryRepository memoCategoryRepository;
    private final MemoImageRepository memoImageRepository;

    public void saveMemoCategory(MemoCategory memoCategory) {
        memoCategoryRepository.save(memoCategory);
    }
}
