package kr.co.fitapet.domain.domains.member.repository;

import com.kcy.fitapet.domain.member.domain.QManager;
import com.kcy.fitapet.domain.member.domain.QMember;
import com.kcy.fitapet.domain.pet.domain.QPet;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
