package kr.co.fitapet.domain.domains.invitation.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.fitapet.domain.domains.invitation.domain.ManagerInvitation;
import kr.co.fitapet.domain.domains.invitation.domain.QManagerInvitation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ManagerInvitationRepositoryImpl implements ManagerInvitationRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final QManagerInvitation invitation = QManagerInvitation.managerInvitation;

    @Override
    public boolean isExpired(Long invitationId) {
        return queryFactory.selectOne()
                .from(invitation)
                .where(invitation.id.eq(invitationId)
                        .and(invitation.expireDate.after(LocalDateTime.now())))
                .fetchFirst() == null;
    }

    @Override
    public boolean isExistsAndNotExpired(Long petId, Long toId) {
        return queryFactory.selectOne()
                .from(invitation)
                .where(
                        invitation.pet.id.eq(petId)
                                .and(invitation.to.id.eq(toId))
                                .and(invitation.expireDate.after(LocalDateTime.now()))
                )
                .fetchFirst() != null;
    }

    @Override
    public List<ManagerInvitation> findAllByPetIdNotExpiredAndNotAccepted(Long petId) {
        return queryFactory.selectFrom(invitation)
                .where(
                        invitation.pet.id.eq(petId)
                                .and(invitation.expireDate.after(LocalDateTime.now()))
                                .and(invitation.isAccepted.eq(Boolean.FALSE))
                )
                .fetch();
    }

    @Override
    public List<ManagerInvitation> findAllByToIdNotExpiredAndNotAccepted(Long toId) {
        return queryFactory.selectFrom(invitation)
                .where(
                        invitation.to.id.eq(toId)
                                .and(invitation.expireDate.after(LocalDateTime.now()))
                                .and(invitation.isAccepted.eq(Boolean.FALSE))
                )
                .fetch();
    }
}
