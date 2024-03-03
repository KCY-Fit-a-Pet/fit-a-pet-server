package kr.co.fitapet.domain.domains.invitation.repository;

import kr.co.fitapet.domain.domains.invitation.domain.ManagerInvitation;

import java.util.List;

public interface ManagerInvitationRepositoryCustom {
    boolean isExpired(Long invitationId);
    boolean isExistsAndNotExpired(Long petId, Long toId);
    List<ManagerInvitation> findAllByPetIdNotExpiredAndNotAccepted(Long petId);
    List<ManagerInvitation> findAllByToIdNotExpiredAndNotAccepted(Long toId);
}
