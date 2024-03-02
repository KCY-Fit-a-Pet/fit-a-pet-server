package kr.co.fitapet.api.apis.notification.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "알림 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/notifications")
public class NotificationApi {
}
