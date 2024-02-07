package kr.co.fitapet.api.apis.pet.usecase;

import kr.co.fitapet.api.common.security.jwt.exception.AuthErrorCode;
import kr.co.fitapet.api.common.security.jwt.exception.AuthErrorException;
import kr.co.fitapet.common.annotation.UseCase;
import kr.co.fitapet.domain.domains.care.service.CareSearchService;
import kr.co.fitapet.domain.domains.member.domain.Manager;
import kr.co.fitapet.domain.domains.member.domain.Member;
import kr.co.fitapet.domain.domains.member.service.MemberSaveService;
import kr.co.fitapet.domain.domains.member.service.MemberSearchService;
import kr.co.fitapet.domain.domains.member.type.ManageType;
import kr.co.fitapet.domain.domains.memo.domain.MemoCategory;
import kr.co.fitapet.domain.domains.pet.domain.Pet;
import kr.co.fitapet.domain.domains.pet.dto.PetInfoRes;
import kr.co.fitapet.domain.domains.pet.service.PetSaveService;
import kr.co.fitapet.domain.domains.pet.service.PetSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@UseCase
@RequiredArgsConstructor
public class PetUseCase {
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
            throw new AuthErrorException(AuthErrorCode.FORBIDDEN_ACCESS_TOKEN, "관리자 권한이 없습니다.");

        return careSearchService.checkCategoryExist(categoryName, petIds);
    }

    @Transactional(readOnly = true)
    public PetInfoRes getPets(Long userId) {
        List<Manager> managers = memberSearchService.findAllManagerByMemberId(userId);
        List<Pet> pets = managers.stream().map(Manager::getPet).toList();
        return PetInfoRes.ofPetInfo(pets);
    }
}
