package kr.co.fitapet.domain.domains.memo.repository;

import com.kcy.fitapet.domain.memo.domain.Memo;
import com.kcy.fitapet.global.common.repository.ExtendedRepository;

public interface MemoRepository extends ExtendedRepository<Memo, Long>, MemoQueryDslRepository {
    boolean existsByIdAndMemoCategory_Id(Long memoId, Long memoCategoryId);
}
