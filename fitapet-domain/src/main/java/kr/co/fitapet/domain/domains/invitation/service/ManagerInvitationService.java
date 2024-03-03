package kr.co.fitapet.domain.domains.invitation.service;

import kr.co.fitapet.common.annotation.DomainService;
import kr.co.fitapet.domain.domains.invitation.domain.ManagerInvitation;
import kr.co.fitapet.domain.domains.invitation.repository.ManagerInvitationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(readOnly = true)
    public ManagerInvitation findById(Long id) {
        return managerInvitationRepository.findByIdOrElseThrow(id);
    }

    @Transactional(readOnly = true)
    public List<ManagerInvitation> findAllByPetIdNotExpiredAndNotAccepted(Long petId) {
        return managerInvitationRepository.findAllByPetIdNotExpiredAndNotAccepted(petId);
    }

    @Transactional(readOnly = true)
    public List<ManagerInvitation> findAllByToIdNotExpiredAndNotAccepted(Long memberId) {
        return managerInvitationRepository.findAllByToIdNotExpiredAndNotAccepted(memberId);
    }

    @Transactional(readOnly = true)
    public boolean isExists(Long toId, Long petId, Long invitationId) {
        return managerInvitationRepository.existsByIdAndTo_IdAndPet_Id(invitationId, toId, petId);
    }

    @Transactional(readOnly = true)
    public boolean isExists(Long petId, Long invitationId) {
        return managerInvitationRepository.existsByIdAndPet_Id(invitationId, petId);
    }

    @Transactional(readOnly = true)
    public boolean isExpired(Long invitationId) {
        return managerInvitationRepository.isExpired(invitationId);
    }

    @Transactional(readOnly = true)
    public boolean isExistsAndNotExpired(Long petId, Long toId) {
        return managerInvitationRepository.isExistsAndNotExpired(petId, toId);
    }

    @Transactional
    public void delete(ManagerInvitation managerInvitation) {
        managerInvitationRepository.delete(managerInvitation);
    }
}
