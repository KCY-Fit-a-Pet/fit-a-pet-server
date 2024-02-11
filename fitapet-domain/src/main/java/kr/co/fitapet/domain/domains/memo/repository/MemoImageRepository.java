package kr.co.fitapet.domain.domains.memo.repository;


import kr.co.fitapet.domain.common.repository.ExtendedRepository;
import kr.co.fitapet.domain.domains.memo.domain.MemoImage;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemoImageRepository extends ExtendedRepository<MemoImage, Long> {
    List<MemoImage> findByMemo_Id(Long memoId);
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from MemoImage mi where mi in :memoImages")
    void deleteAll(List<MemoImage> memoImages);
}
