package kr.co.fitapet.api.apis.manager.usecase;

import kr.co.fitapet.api.apis.manager.dto.ManagerInfoRes;
import kr.co.fitapet.common.annotation.UseCase;
import kr.co.fitapet.domain.domains.manager.service.ManagerSearchService;
import kr.co.fitapet.domain.domains.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@UseCase
@RequiredArgsConstructor
public class ManagerUseCase {
    private final ManagerSearchService managerSearchService;

    @Transactional(readOnly = true)
    public List<ManagerInfoRes> findManagers(Long petId) {
        Long masterId = managerSearchService.findMasterIdByPetId(petId);
        List<Member> managers = managerSearchService.findAllManagerByPetId(petId);
        return managers.stream().map(manager -> ManagerInfoRes.valueOf(manager, masterId)).toList();
    }
}
