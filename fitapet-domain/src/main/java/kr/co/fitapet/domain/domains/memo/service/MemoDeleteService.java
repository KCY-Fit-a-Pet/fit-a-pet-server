package kr.co.fitapet.domain.domains.memo.service;

import kr.co.fitapet.common.annotation.DomainService;
import kr.co.fitapet.domain.domains.memo.domain.Memo;
import kr.co.fitapet.domain.domains.memo.domain.MemoImage;
import kr.co.fitapet.domain.domains.memo.repository.MemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@DomainService
@RequiredArgsConstructor
public class MemoDeleteService {
    private final MemoRepository memoRepository;

    @Transactional
    public void deleteMemoById(Long memoId) {
        memoRepository.deleteById(memoId);
    }

    @Transactional
    public void deleteMemoAndMemoImages(Memo memo, List<MemoImage> memoImages) {
        memoImages.forEach(memoImage -> memoImage.updateMemo(null));
        memoRepository.delete(memo);
    }
}
