package kr.co.fitapet.domain.domains.memo.repository;


import kr.co.fitapet.domain.common.repository.ExtendedRepository;
import kr.co.fitapet.domain.domains.memo.domain.MemoImage;

import java.util.List;

public interface MemoImageRepository extends ExtendedRepository<MemoImage, Long> {
    List<MemoImage> findByMemo_Id(Long memoId);
}
