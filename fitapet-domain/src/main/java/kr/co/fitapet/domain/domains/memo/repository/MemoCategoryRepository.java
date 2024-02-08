package kr.co.fitapet.domain.domains.memo.repository;


import kr.co.fitapet.domain.common.repository.ExtendedRepository;
import kr.co.fitapet.domain.domains.memo.domain.MemoCategory;

public interface MemoCategoryRepository extends ExtendedRepository<MemoCategory, Long>, MemoCategoryQueryDslRepository {
    boolean existsByIdAndPet_Id(Long memoCategoryId, Long petId);
    boolean existsByIdAndPet_IdAndParentIsNull(Long memoCategoryId, Long petId);
    boolean existsByIdAndPet_IdAndParentIsNotNull(Long memoCategoryId, Long petId);

}
