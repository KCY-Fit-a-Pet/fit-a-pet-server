package kr.co.fitapet.domain.domains.invitation.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.fitapet.domain.domains.invitation.domain.QInvitation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ManagerManagerInvitationQueryDslRepositoryDsl implements ManagerInvitationQueryDslRepository {
    private final JPAQueryFactory queryFactory;
    private final QInvitation invitation = QInvitation.invitation;

}
