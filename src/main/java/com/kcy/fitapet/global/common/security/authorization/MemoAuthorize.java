package com.kcy.fitapet.global.common.security.authorization;

import com.kcy.fitapet.domain.memo.dao.MemoCategoryRepository;
import com.kcy.fitapet.domain.memo.dao.MemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("memoAuthorize")
@RequiredArgsConstructor
public class MemoAuthorize {
    private final MemoRepository memoRepository;
    private final MemoCategoryRepository memoCategoryRepository;

    public boolean isValidMemoCategory(Long memoCategoryId, Long petId) {
        return memoCategoryRepository.existsByIdAndPet_Id(memoCategoryId, petId);
    }

    public boolean isValidRootMemoCategory(Long memoCategoryId, Long petId) {
        return memoCategoryRepository.existsByIdAndPet_IdAndParentIsNull(memoCategoryId, petId);
    }

    public boolean isValidSubMemoCategory(Long memoCategoryId, Long petId) {
        return memoCategoryRepository.existsByIdAndPet_IdAndParentIsNotNull(memoCategoryId, petId);
    }

    public boolean isValidMemoCategoryAndMemo(Long memoCategoryId, Long memoId, Long petId) {
        return isValidMemoCategory(memoCategoryId, petId) && isValidMemo(memoId, memoCategoryId);
    }

    private boolean isValidMemo(Long memoId, Long memoCategoryId) {
        return memoRepository.existsByIdAndMemoCategory_Id(memoId, memoCategoryId);
    }
}
