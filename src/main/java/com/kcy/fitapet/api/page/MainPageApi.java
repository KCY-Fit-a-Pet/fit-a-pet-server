package com.kcy.fitapet.api.page;

import com.kcy.fitapet.api.page.service.MainPageService;
import com.kcy.fitapet.global.common.security.authentication.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Slf4j
public class MainPageApi {
    private final MainPageService mainPageService;

    @GetMapping("")
    public ResponseEntity<?> getMainPage(@AuthenticationPrincipal CustomUserDetails user) {

        return ResponseEntity.ok().build();
    }
}
