package kr.co.fitapet.api.apis.test;

import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.fitapet.api.common.security.authentication.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "푸시 테스트 API", description = "push notification을 위한 개발자용 임시 API 입니다. 테스트 후 삭제 예정")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v2/test/members")
public class NotificationTestController {
    private final NotificationTestService notificationTestService;

    @GetMapping("/push")
    @PreAuthorize("isAuthenticated()")
    public void push(@AuthenticationPrincipal CustomUserDetails user) {
        notificationTestService.push(user.getUserId());
    }
}
