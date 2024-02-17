package kr.co.fitapet.domain.domains.member.service;

import kr.co.fitapet.common.annotation.DomainService;
import kr.co.fitapet.domain.domains.member.domain.Member;
import kr.co.fitapet.domain.domains.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@DomainService
@RequiredArgsConstructor
public class MemberSaveService {
    private final MemberRepository memberRepository;

    @Transactional
    public Member saveMember(Member member) {
        return memberRepository.save(member);
    }
}
