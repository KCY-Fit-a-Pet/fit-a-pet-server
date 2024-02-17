package kr.co.fitapet.domain.domains.member.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.fitapet.domain.common.util.QueryDslUtil;
import kr.co.fitapet.domain.domains.manager.domain.QManager;
import kr.co.fitapet.domain.domains.member.domain.Member;
import kr.co.fitapet.domain.domains.member.domain.QMember;
import kr.co.fitapet.domain.domains.member.domain.QMemberNickname;
import kr.co.fitapet.domain.domains.member.dto.MemberInfo;
import kr.co.fitapet.domain.domains.pet.domain.QPet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberQueryDslRepositoryImpl implements MemberQueryDslRepository {
    private final JPAQueryFactory queryFactory;
    private final QMember member = QMember.member;
    private final QMemberNickname nickname = QMemberNickname.memberNickname;
    private final QManager manager = QManager.manager;
    private final QPet pet = QPet.pet;

    @Override
    public List<Member> findByIds(List<Long> ids) {
        return queryFactory.selectFrom(member)
                .where(member.id.in(ids))
                .fetch();
    }

    @Override
    public List<Long> findMyPetIds(Long memberId) {
        return queryFactory.select(pet.id)
                .from(member)
                .leftJoin(manager).on(manager.member.id.eq(member.id))
                .leftJoin(pet).on(pet.id.eq(manager.pet.id))
                .where(member.id.eq(memberId))
                .fetch();
    }

    @Override
    public Optional<MemberInfo> findMemberInfo(Long requesterId, String target) {
        return Optional.ofNullable(
                queryFactory
                        .select(
                                Projections.constructor(
                                        MemberInfo.class,
                                        member.id, member.uid, nickname.nickname.coalesce(member.name), member.profileImg
                                )
                        )
                        .from(member)
                        .leftJoin(nickname).on(member.id.eq(nickname.to.id).and(nickname.from.id.eq(requesterId)))
                        .where(QueryDslUtil.matchAgainstOneElemNaturalMode(member.uid, target))
                        .fetchOne()
        );
    }

    @Override
    public List<MemberInfo> findMemberInfos(List<Long> memberIds, Long requesterId) {
        return queryFactory
                .select(
                        Projections.constructor(
                                MemberInfo.class,
                                member.id, member.uid,
                                nickname.nickname.coalesce(member.name), member.profileImg
                        )
                )
                .from(member)
                .leftJoin(nickname).on(member.id.eq(nickname.to.id).and(nickname.from.id.eq(requesterId)))
                .where(member.id.in(memberIds))
                .fetch();
    }
}
