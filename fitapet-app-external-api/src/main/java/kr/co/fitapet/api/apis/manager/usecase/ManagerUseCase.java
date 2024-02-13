package kr.co.fitapet.api.apis.manager.usecase;

import kr.co.fitapet.common.annotation.UseCase;
import kr.co.fitapet.domain.domains.manager.dto.ManagerInfoRes;
import kr.co.fitapet.domain.domains.manager.service.ManagerSearchService;
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
    public List<ManagerInfoRes> findManagers(Long petId, Long memberId) {
        return managerSearchService.findAllManagerByPetId(petId, memberId);
    }
}
