package kr.co.fitapet.domain.domains.memo.service;

import kr.co.fitapet.common.annotation.DomainService;
import kr.co.fitapet.domain.domains.memo.repository.MemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@DomainService
@RequiredArgsConstructor
public class MemoDeleteService {
    private final MemoRepository memoRepository;

    @Transactional
    public void deleteMemoById(Long memoId) {
        memoRepository.deleteById(memoId);
    }
}
