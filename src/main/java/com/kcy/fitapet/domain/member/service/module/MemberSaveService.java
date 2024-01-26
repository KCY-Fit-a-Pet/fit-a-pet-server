package com.kcy.fitapet.domain.member.service.module;

import com.kcy.fitapet.domain.member.dao.ManagerJpaRepository;
import com.kcy.fitapet.domain.member.dao.MemberJpaRepository;
import com.kcy.fitapet.domain.member.domain.Manager;
import com.kcy.fitapet.domain.member.domain.Member;
import com.kcy.fitapet.domain.member.type.ManageType;
import com.kcy.fitapet.domain.pet.domain.Pet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberSaveService {
    private final MemberJpaRepository memberRepository;
    private final ManagerJpaRepository managerRepository;

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
