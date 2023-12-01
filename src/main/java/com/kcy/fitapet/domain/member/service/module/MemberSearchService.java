package com.kcy.fitapet.domain.member.service.module;

import com.kcy.fitapet.domain.member.dao.MemberRepository;
import com.kcy.fitapet.domain.member.domain.Member;
import com.kcy.fitapet.domain.member.exception.AccountErrorCode;
import com.kcy.fitapet.global.common.response.exception.GlobalErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberSearchService {
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public Member findById(Long id) {
        return memberRepository.findByIdOrElseThrow(id);
    }

    @Transactional(readOnly = true)
    public Member findByUid(String uid) {
        return memberRepository.findByUid(uid).orElseThrow(
                () -> new GlobalErrorException(AccountErrorCode.NOT_FOUND_MEMBER_ERROR)
        );
    }

    @Transactional(readOnly = true)
    public Member findByPhone(String phone) {
        return memberRepository.findByPhone(phone).orElseThrow(
                () -> new GlobalErrorException(AccountErrorCode.NOT_FOUND_MEMBER_ERROR)
        );
    }

    @Transactional(readOnly = true)
    public boolean isExistByUidOrEmailOrPhone(String uid, String email, String phone) {
        return memberRepository.existsByUidOrEmailOrPhone(uid, email, phone);
    }

    @Transactional(readOnly = true)
    public boolean isExistByPhone(String phone) {
        return memberRepository.existsByPhone(phone);
    }
}
