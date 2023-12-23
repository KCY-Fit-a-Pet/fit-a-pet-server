package com.kcy.fitapet.domain.oauth.api;

import com.kcy.fitapet.domain.member.dto.auth.SignInReq;
import com.kcy.fitapet.domain.oauth.dto.OauthSignInReq;
import com.kcy.fitapet.domain.oauth.dto.OauthSignUpReq;
import com.kcy.fitapet.domain.oauth.service.OAuthService;
import com.kcy.fitapet.domain.oauth.type.ProviderType;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "OAuth API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth/oauth")
@Slf4j
public class OAuthApi {
    private final OAuthService oAuthService;

    @PostMapping("")
    @PreAuthorize("isAnonymous()")
    public void signIn(
            @RequestParam("provider") ProviderType provider,
            @RequestBody @Valid OauthSignInReq req
    ) {
        if (ProviderType.NAVER.equals(provider)) {

        } else {

        }
    }

    @PostMapping("/{id}")
    @PreAuthorize("isAnonymous()")
    public void signUp(
            @PathVariable("id") String id,
            @RequestParam("provider") ProviderType provider,
            @RequestBody @Valid OauthSignUpReq req
    ) {

    }
}
