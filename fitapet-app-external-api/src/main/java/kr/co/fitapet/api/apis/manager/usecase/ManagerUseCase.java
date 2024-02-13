package kr.co.fitapet.api.apis.manager.usecase;

import kr.co.fitapet.common.annotation.UseCase;
import kr.co.fitapet.domain.domains.member.service.MemberSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UseCase
@RequiredArgsConstructor
public class ManagerUseCase {
    private final MemberSearchService memberSearchService;
}
