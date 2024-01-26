package com.kcy.fitapet.domain.memo.service.component;

import com.kcy.fitapet.domain.memo.service.module.MemoSaveService;
import com.kcy.fitapet.domain.memo.service.module.MemoSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemoManageService {
    private final MemoSearchService memoSearchService;
    private final MemoSaveService memoSaveService;
}
