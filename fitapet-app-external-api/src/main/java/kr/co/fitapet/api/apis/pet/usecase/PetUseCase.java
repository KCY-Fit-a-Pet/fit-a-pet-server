package kr.co.fitapet.api.apis.pet.usecase;

import kr.co.fitapet.api.apis.pet.dto.PetDetailRes;
import kr.co.fitapet.api.apis.pet.helper.PetCareHelper;
import kr.co.fitapet.api.apis.pet.mapper.PetImageSearchMapper;
import kr.co.fitapet.api.apis.pet.mapper.PetManagerMapper;
import kr.co.fitapet.api.common.security.jwt.exception.AuthErrorCode;
import kr.co.fitapet.api.common.security.jwt.exception.AuthErrorException;
import kr.co.fitapet.common.annotation.UseCase;
import kr.co.fitapet.domain.domains.memo.domain.MemoCategory;
import kr.co.fitapet.domain.domains.pet.domain.Pet;
import kr.co.fitapet.domain.domains.pet.dto.PetInfoRes;
import kr.co.fitapet.domain.domains.pet.dto.PetSaveReq;
import kr.co.fitapet.domain.domains.pet.service.PetDeleteService;
import kr.co.fitapet.domain.domains.pet.service.PetSaveService;
import kr.co.fitapet.domain.domains.pet.service.PetSearchService;
import kr.co.fitapet.infra.common.event.ObjectStorageImageDeleteEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@UseCase
@Slf4j
@RequiredArgsConstructor
public class PetUseCase {
    private final PetManagerMapper petManagerMapper;
    private final PetImageSearchMapper petImageSearchMapper;

    private final PetSearchService petSearchService;
    private final PetSaveService petSaveService;
    private final PetDeleteService petDeleteService;
    private final PetCareHelper petCareHelper;

    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void savePet(Pet pet, Long memberId) {
        pet = petSaveService.savePet(pet);

        MemoCategory.ofRootInstance(pet.getPetName(), pet);
        petManagerMapper.mappingMemberAndPet(memberId, pet);
    }

    @Transactional(readOnly = true)
    public PetInfoRes findPetsSummaryByUserId(Long userId) {
        List<Pet> pets = petManagerMapper.findAllPetByMemberId(userId);
        return PetInfoRes.ofSummary(pets);
    }

    @Transactional(readOnly = true)
    public List<?> checkCategoryExist(Long userId, String categoryName, List<Long> petIds) {
        if (!petManagerMapper.isManagerAll(userId, petIds))
            throw new AuthErrorException(AuthErrorCode.FORBIDDEN_ACCESS_TOKEN, "관리자 권한이 없습니다.");

        return petCareHelper.checkCategoryExist(categoryName, petIds);
    }

    @Transactional(readOnly = true)
    public PetInfoRes findPets(Long userId) {
        List<Pet> pets = petManagerMapper.findAllManagerByMemberId(userId);
        return PetInfoRes.ofPetInfo(pets);
    }

    @Transactional(readOnly = true)
    public PetDetailRes findPet(Long petId) {
        Pet pet = petSearchService.findPetById(petId);
        return PetDetailRes.from(pet);
    }

    @Transactional
    public void updatePet(Long petId, PetSaveReq req) {
        Pet pet = petSearchService.findPetById(petId);
        pet.updatePet(req.toEntity());
        petSaveService.savePet(pet);
    }

    @Transactional
    @CacheEvict(value = "master", key = "#masterId + '@' + #petId", cacheManager = "managerCacheManager")
    public void deletePet(Long petId, Long masterId) {
        log.info("deletePet: {}", petId);
        List<String> deleteImageUrls = petImageSearchMapper.findPetProfileImageAndMemoImageUrls(petId);
        log.info("deleteImageUrls: {}", deleteImageUrls);
        petDeleteService.deletePet(petId);
        eventPublisher.publishEvent(ObjectStorageImageDeleteEvent.valueOf(deleteImageUrls));
    }
}
