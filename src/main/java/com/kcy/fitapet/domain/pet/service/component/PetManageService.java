package com.kcy.fitapet.domain.pet.service.component;

import com.kcy.fitapet.domain.care.domain.Care;
import com.kcy.fitapet.domain.care.service.CareSaveService;
import com.kcy.fitapet.domain.member.service.module.MemberSearchService;
import com.kcy.fitapet.domain.pet.domain.Pet;
import com.kcy.fitapet.domain.pet.dto.PetRegisterReq;
import com.kcy.fitapet.domain.pet.service.module.PetSaveService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PetManageService {
    private final MemberSearchService memberSearchService;
    private final PetSaveService petSaveService;
    private final CareSaveService careSaveService;

    @Transactional
    public void savePet(PetRegisterReq req) {
        Pet pet = petSaveService.savePet(req.toPetEntity());
        // TODO : user - pet 매핑
        List<Care> cares = careSaveService.saveCares(req.toCareEntity());
        petSaveService.mappingPetAndCares(pet, cares);
        // TODO: cares에 대한 careDetail 저장
    }
}
