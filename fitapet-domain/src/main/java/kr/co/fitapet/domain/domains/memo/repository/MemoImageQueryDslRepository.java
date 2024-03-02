package kr.co.fitapet.domain.domains.memo.repository;

import java.util.List;

public interface MemoImageQueryDslRepository {
    List<String> findMemoImageUrlsByMemoIds(List<Long> memoIds);
}
