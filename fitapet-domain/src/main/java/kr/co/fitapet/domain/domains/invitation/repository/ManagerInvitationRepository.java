package kr.co.fitapet.domain.domains.invitation.repository;

import kr.co.fitapet.domain.common.repository.ExtendedRepository;
import kr.co.fitapet.domain.domains.invitation.domain.ManagerInvitation;

public interface ManagerInvitationRepository extends ExtendedRepository<ManagerInvitation, Long>, ManagerInvitationRepositoryCustom {
    boolean existsByIdAndTo_IdAndPet_Id(Long invitationId, Long toId, Long petId);
    boolean existsByIdAndPet_Id(Long invitationId, Long petId);
}
