package kr.co.fitapet.domain.domains.memo.service;

import kr.co.fitapet.common.annotation.DomainService;
import kr.co.fitapet.domain.domains.memo.domain.Memo;
import kr.co.fitapet.domain.domains.memo.domain.MemoCategory;
import kr.co.fitapet.domain.domains.memo.repository.MemoCategoryRepository;
import kr.co.fitapet.domain.domains.memo.repository.MemoImageRepository;
import kr.co.fitapet.domain.domains.memo.repository.MemoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@DomainService
@Slf4j
@RequiredArgsConstructor
public class MemoSaveService {
    private final MemoRepository memoRepository;
    private final MemoCategoryRepository memoCategoryRepository;
    private final MemoImageRepository memoImageRepository;

    public void saveMemoCategory(MemoCategory memoCategory) {
        memoCategoryRepository.save(memoCategory);
    }

    public void saveMemo(Memo memo) {
        memoRepository.save(memo);
    }
}
