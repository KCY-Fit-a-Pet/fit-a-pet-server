package kr.co.fitapet.domain.domains.member.service;

import kr.co.fitapet.common.annotation.DomainService;
import kr.co.fitapet.domain.domains.member.domain.Manager;
import kr.co.fitapet.domain.domains.member.domain.Member;
import kr.co.fitapet.domain.domains.member.repository.ManagerRepository;
import kr.co.fitapet.domain.domains.member.repository.MemberRepository;
import kr.co.fitapet.domain.domains.member.type.ManageType;
import kr.co.fitapet.domain.domains.pet.domain.Pet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DomainService
@RequiredArgsConstructor
public class MemberSaveService {
    private final MemberRepository memberRepository;
    private final ManagerRepository managerRepository;

    @Transactional
    public Member saveMember(Member member) {
        return memberRepository.save(member);
    }

    @Transactional
    public void mappingMemberAndPet(Member member, Pet pet, ManageType manageType) {
        Manager manager = Manager.of(member, pet, false, manageType);
        managerRepository.save(manager);
    }
}
