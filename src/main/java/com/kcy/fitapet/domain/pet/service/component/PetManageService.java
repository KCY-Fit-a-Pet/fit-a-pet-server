package com.kcy.fitapet.domain.pet.service.component;

import com.kcy.fitapet.domain.member.domain.Member;
import com.kcy.fitapet.domain.member.service.module.MemberSaveService;
import com.kcy.fitapet.domain.member.service.module.MemberSearchService;
import com.kcy.fitapet.domain.member.type.ManageType;
import com.kcy.fitapet.domain.pet.domain.Pet;
import com.kcy.fitapet.domain.pet.service.module.PetSaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PetManageService {
    private final MemberSearchService memberSearchService;
    private final MemberSaveService memberSaveService;
    private final PetSaveService petSaveService;

    @Transactional
    public void savePet(Pet pet, Long memberId) {
        pet = petSaveService.savePet(pet);

        Member member = memberSearchService.getMemberById(memberId);
        memberSaveService.mappingMemberAndPet(member, pet, ManageType.MASTER);
    }


}
