package com.kcy.fitapet.domain.member.service.module;

import com.kcy.fitapet.domain.member.dao.MemberRepository;
import com.kcy.fitapet.domain.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberSearchService {
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public Member getMemberById(Long id) {
        return memberRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 회원이 존재하지 않습니다.")
        );
    }

    @Transactional(readOnly = true)
    public Member getMemberByUid(String uid) {
        return memberRepository.findByUid(uid).orElseThrow(
                () -> new IllegalArgumentException("해당 회원이 존재하지 않습니다.")
        );
    }

    public boolean isExistMemberByUidOrEmailOrPhone(String uid, String email, String phone) {
        return memberRepository.existsByUidOrEmailOrPhone(uid, email, phone);
    }

    public boolean isExistMemberByPhone(String phone) {
        return memberRepository.existsByPhone(phone);
    }
}
