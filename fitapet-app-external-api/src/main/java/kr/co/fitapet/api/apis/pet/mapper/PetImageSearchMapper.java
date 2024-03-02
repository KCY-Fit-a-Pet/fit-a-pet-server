package kr.co.fitapet.api.apis.pet.mapper;

import kr.co.fitapet.common.annotation.Mapper;
import kr.co.fitapet.domain.domains.memo.service.MemoSearchService;
import kr.co.fitapet.domain.domains.pet.domain.Pet;
import kr.co.fitapet.domain.domains.pet.service.PetSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Mapper
@RequiredArgsConstructor
public class PetImageSearchMapper {
    private final PetSearchService petSearchService;
    private final MemoSearchService memoSearchService;

    /**
     * 반려동물 프로필 이미지와 메모에 등록된 모든 이미지 조회
     */
    @Transactional(readOnly = true)
    public List<String> findPetProfileImageAndMemoImageUrls(Long petId) {
        List<String> images = new ArrayList<>();

        Pet pet = petSearchService.findPetById(petId);
        if (pet.getPetProfileImg() != null)
            images.add(pet.getPetProfileImg());
        List<Long> memoIds = memoSearchService.findMemoIdsByPetId(pet.getId());
        List<String> memoImageUrls = memoSearchService.findMemoImageUrlsByMemoIds(memoIds);
        images.addAll(memoImageUrls);

        return images;
    }
}
