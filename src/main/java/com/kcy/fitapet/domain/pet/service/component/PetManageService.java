package com.kcy.fitapet.domain.pet.service.component;

import com.kcy.fitapet.domain.care.service.module.CareSearchService;
import com.kcy.fitapet.domain.member.domain.Manager;
import com.kcy.fitapet.domain.member.domain.Member;
import com.kcy.fitapet.domain.member.service.module.MemberSaveService;
import com.kcy.fitapet.domain.member.service.module.MemberSearchService;
import com.kcy.fitapet.domain.member.type.ManageType;
import com.kcy.fitapet.domain.memo.domain.MemoCategory;
import com.kcy.fitapet.domain.pet.domain.Pet;
import com.kcy.fitapet.domain.pet.dto.PetInfoRes;
import com.kcy.fitapet.domain.pet.service.module.PetSaveService;
import com.kcy.fitapet.domain.pet.service.module.PetSearchService;
import com.kcy.fitapet.global.common.response.exception.GlobalErrorException;
import com.kcy.fitapet.global.common.security.jwt.exception.AuthErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PetManageService {
    private final MemberSearchService memberSearchService;
    private final MemberSaveService memberSaveService;
    private final PetSaveService petSaveService;
    private final PetSearchService petSearchService;
    private final CareSearchService careSearchService;

    @Transactional
    public void savePet(Pet pet, Long memberId) {
        pet = petSaveService.savePet(pet);

        MemoCategory.ofRootInstance(pet.getPetName(), pet);

        Member member = memberSearchService.findById(memberId);
        memberSaveService.mappingMemberAndPet(member, pet, ManageType.MASTER);
    }

    @Transactional(readOnly = true)
    public PetInfoRes findPetsSummaryByUserId(Long userId) {
        List<Pet> pets = memberSearchService.findAllManagerByMemberId(userId).stream().map(Manager::getPet).toList();
        return PetInfoRes.ofSummary(pets);
    }

    @Transactional(readOnly = true)
    public List<?> checkCategoryExist(Long userId, String categoryName, List<Long> petIds) {
        if (!memberSearchService.isManagerAll(userId, petIds))
            throw new GlobalErrorException(AuthErrorCode.FORBIDDEN_ACCESS_TOKEN);

        return careSearchService.checkCategoryExist(categoryName, petIds);
    }

    @Transactional(readOnly = true)
    public PetInfoRes getPets(Long userId) {
        List<Manager> managers = memberSearchService.findAllManagerByMemberId(userId);
        List<Pet> pets = managers.stream().map(Manager::getPet).toList();
        return PetInfoRes.ofPetInfo(pets);
    }
}
