package kr.co.fitapet.domain.domains.manager.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QList;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.fitapet.domain.domains.manager.domain.QManager;
import kr.co.fitapet.domain.domains.manager.dto.ManagerInfoRes;
import kr.co.fitapet.domain.domains.manager.type.ManageType;
import kr.co.fitapet.domain.domains.member.domain.Member;
import kr.co.fitapet.domain.domains.member.domain.QMember;
import kr.co.fitapet.domain.domains.member.domain.QMemberNickname;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ManagerQueryDslRepositoryImpl implements ManagerQueryDslRepository {
    private final JPAQueryFactory queryFactory;
    private final QMember member = QMember.member;
    private final QManager manager = QManager.manager;
    private final QMemberNickname nickname = QMemberNickname.memberNickname;

    @Override
    public Long findMasterIdByPetId(Long petId) {
        return queryFactory.select(manager.member.id)
                .from(manager)
                .where(manager.pet.id.eq(petId)
                        .and(manager.manageType.eq(ManageType.MASTER)))
                .fetchFirst();
    }

    @Override
    public List<ManagerInfoRes> findAllManager(Long petId, Long memberId) {
        return queryFactory
                .select(
                        Projections.constructor(
                                ManagerInfoRes.class,
                                member.id, member.uid,
                                nickname.nickname.coalesce(member.name),
                                member.profileImg,
                                new CaseBuilder().when(manager.manageType.eq(ManageType.MASTER)).then(true).otherwise(false)
                        )
                )
                .from(manager)
                .leftJoin(member).on(manager.member.id.eq(member.id))
                .leftJoin(nickname).on(member.id.eq(nickname.to.id))
                .where(manager.pet.id.eq(petId))
                .fetch();
    }
}
