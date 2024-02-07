package kr.co.fitapet.domain.domains.oauth.service;

import kr.co.fitapet.common.annotation.DomainService;
import kr.co.fitapet.common.execption.GlobalErrorException;
import kr.co.fitapet.domain.domains.member.domain.Member;
import kr.co.fitapet.domain.domains.oauth.domain.OauthAccount;
import kr.co.fitapet.domain.domains.oauth.exception.OauthException;
import kr.co.fitapet.domain.domains.oauth.repository.OauthRepository;
import kr.co.fitapet.domain.domains.oauth.type.ProviderType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DomainService
@RequiredArgsConstructor
public class OauthSearchService {
    private final OauthRepository oauthRepository;

    @Transactional(readOnly = true)
    public boolean isExistMember(String oauthId, ProviderType provider) {
        return oauthRepository.existsByOauthIdAndProvider(oauthId, provider);
    }

    @Transactional(readOnly = true)
    public boolean isExistEmail(String email) {
        return oauthRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public Member findMemberByOauthIdAndProvider(String oauthId, ProviderType provider) {
        OauthAccount oauthAccount = oauthRepository.findByOauthIdAndProvider(oauthId, provider)
                .orElseThrow(() -> new GlobalErrorException(OauthException.NOT_FOUND_MEMBER));
        return oauthAccount.getMember();
    }
}
