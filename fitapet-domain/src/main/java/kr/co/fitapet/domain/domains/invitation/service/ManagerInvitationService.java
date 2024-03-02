package kr.co.fitapet.domain.domains.invitation.service;

import kr.co.fitapet.common.annotation.DomainService;
import kr.co.fitapet.domain.domains.invitation.domain.ManagerInvitation;
import kr.co.fitapet.domain.domains.invitation.repository.ManagerInvitationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@DomainService
@RequiredArgsConstructor
public class ManagerInvitationService {
    private final ManagerInvitationRepository managerInvitationRepository;

    @Transactional
    public void save(ManagerInvitation managerInvitation) {
        managerInvitationRepository.save(managerInvitation);
    }

//    @Transactional
//    public List<ManagerInvitation> findAllByToIdWhenNotExpired(Long memberId) {
//        return managerInvitationRepository.findAll(memberId, LocalDateTime.now());
//    }

//    @Transactional
//    public boolean isExpired(Long memberId, Long petId) {
//        managerInvitationRepository.isExpired(memberId, petId);
//    }

    @Transactional
    public void delete(ManagerInvitation managerInvitation) {
        managerInvitationRepository.delete(managerInvitation);
    }
}
