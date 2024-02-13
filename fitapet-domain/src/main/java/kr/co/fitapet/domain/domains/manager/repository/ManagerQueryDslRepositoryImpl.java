package kr.co.fitapet.domain.domains.manager.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.fitapet.domain.domains.manager.domain.QManager;
import kr.co.fitapet.domain.domains.manager.type.ManageType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ManagerQueryDslRepositoryImpl implements ManagerQueryDslRepository {
    private final JPAQueryFactory queryFactory;
    private final QManager manager = QManager.manager;

    @Override
    public Long findMasterIdByPetId(Long petId) {
        return queryFactory.select(manager.member.id)
                .from(manager)
                .where(manager.pet.id.eq(petId)
                        .and(manager.manageType.eq(ManageType.MASTER)))
                .fetchFirst();
    }
}
