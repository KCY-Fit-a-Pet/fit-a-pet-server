package com.kcy.fitapet.domain.member.service.module;

import com.kcy.fitapet.domain.member.dao.MemberRepository;
import com.kcy.fitapet.domain.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberSearchService {
    private final MemberRepository memberRepository;

    public Member getMemberById(Long id) {
        return memberRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 회원이 존재하지 않습니다.")
        );
    }

    public Member getMemberByUid(String uid) {
        return memberRepository.findByUid(uid).orElseThrow(
                () -> new IllegalArgumentException("해당 회원이 존재하지 않습니다.")
        );
    }

    public boolean isExistMemberByUid(String uid) {
        return memberRepository.existsByUid(uid);
    }

    public boolean isExistMemberByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    public boolean isExistMemberByPhone(String phone) {
        return memberRepository.existsByPhone(phone);
    }
}
