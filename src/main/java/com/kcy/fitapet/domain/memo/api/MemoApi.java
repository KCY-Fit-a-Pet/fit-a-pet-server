package com.kcy.fitapet.domain.memo.api;

import com.kcy.fitapet.domain.memo.service.component.MemoManageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "일기 API", description = "일기 관리 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/users/{user_id}")
public class MemoApi {
    private final MemoManageService memoManageService;
}
