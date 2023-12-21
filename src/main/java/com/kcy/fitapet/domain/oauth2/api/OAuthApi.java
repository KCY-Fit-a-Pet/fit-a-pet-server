package com.kcy.fitapet.domain.oauth2.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "OAuth API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth/oauth")
@Slf4j
public class OAuthApi {
}
