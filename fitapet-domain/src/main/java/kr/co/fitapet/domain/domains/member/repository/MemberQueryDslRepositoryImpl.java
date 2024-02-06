package kr.co.fitapet.domain.domains.member.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.fitapet.domain.domains.member.domain.QManager;
import kr.co.fitapet.domain.domains.member.domain.QMember;
import kr.co.fitapet.domain.domains.pet.domain.QPet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberQueryDslRepositoryImpl implements MemberQueryDslRepository {
    private final JPAQueryFactory queryFactory;
    private final QMember member = QMember.member;
    private final QManager manager = QManager.manager;
    private final QPet pet = QPet.pet;

    @Override
    public List<Long> findMyPetIds(Long memberId) {
        return queryFactory.select(pet.id)
                .from(member)
                .leftJoin(manager).on(manager.member.id.eq(member.id))
                .leftJoin(pet).on(pet.id.eq(manager.pet.id))
                .where(member.id.eq(memberId))
                .fetch();
    }
}
