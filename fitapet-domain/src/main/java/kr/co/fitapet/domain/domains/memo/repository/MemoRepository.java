package kr.co.fitapet.domain.domains.memo.repository;


import kr.co.fitapet.domain.common.repository.ExtendedRepository;
import kr.co.fitapet.domain.domains.memo.domain.Memo;

public interface MemoRepository extends ExtendedRepository<Memo, Long>, MemoQueryDslRepository {
    boolean existsByIdAndMemoCategory_Id(Long memoId, Long memoCategoryId);
}
