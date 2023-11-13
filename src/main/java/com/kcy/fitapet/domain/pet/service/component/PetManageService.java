package com.kcy.fitapet.domain.pet.service.component;

import com.kcy.fitapet.domain.care.domain.Care;
import com.kcy.fitapet.domain.care.domain.DayOfWeek;
import com.kcy.fitapet.domain.care.service.CareSaveService;
import com.kcy.fitapet.domain.care.type.CareType;
import com.kcy.fitapet.domain.member.domain.Member;
import com.kcy.fitapet.domain.member.service.module.MemberSaveService;
import com.kcy.fitapet.domain.member.service.module.MemberSearchService;
import com.kcy.fitapet.domain.member.type.ManageType;
import com.kcy.fitapet.domain.pet.domain.Pet;
import com.kcy.fitapet.domain.pet.dto.PetRegisterReq;
import com.kcy.fitapet.domain.pet.service.module.PetSaveService;
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
    private final CareSaveService careSaveService;

    @Transactional
    public void savePet(PetRegisterReq req, Long memberId) {
        Pet pet = petSaveService.savePet(req.toPetEntity());

        List<Care> cares = careSaveService.saveCares(req.toCareEntity());
        petSaveService.mappingPetAndCares(pet, cares);

        Member member = memberSearchService.getMemberById(memberId);
        memberSaveService.mappingMemberAndPet(member, pet, ManageType.MASTER);

        // TODO: cares에 대한 careDetail 저장
        for (Care care : cares) {
            if (care.getDtype().equals(CareType.DAILY)) {
                // 1. careDetail 생성 (careTime=현재시간, limitTime=현재시간, isDone=false)

                // 1-1. 식사라면 아침, 점심, 저녁 3개 생성

                // 3.

                // 2.

            } else {
                // 1. careDetail 생성

                // 2. DayOfWeek 월~일 false로 초기화해서 생성
                DayOfWeek dayOfWeek = DayOfWeek.init();




            }
        }
    }

    private
}
