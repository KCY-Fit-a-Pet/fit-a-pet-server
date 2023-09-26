package com.kcy.fitapet.domain.member.service.module;

import com.kcy.fitapet.domain.member.dao.MemberRepository;
import com.kcy.fitapet.domain.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberSaveService {
    MemberRepository memberRepository;

    public Member saveMember(Member member) {
        return memberRepository.save(member);
    }
}
