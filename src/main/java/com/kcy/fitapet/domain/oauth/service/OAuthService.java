package com.kcy.fitapet.domain.oauth.service;

import com.kcy.fitapet.domain.oauth.dao.OAuthRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuthService {
    private final OAuthRepository oAuthRepository;

    @Transactional
    public void signUpByOIDC() {

    }

    @Transactional
    public void signInByOIDC() {

    }

    @Transactional
    public void signInByCode() {

    }

    @Transactional
    public void signUpByCode() {

    }
}
