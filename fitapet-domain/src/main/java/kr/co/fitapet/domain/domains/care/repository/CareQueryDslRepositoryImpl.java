package kr.co.fitapet.domain.domains.care.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.fitapet.domain.domains.care.domain.QCare;
import kr.co.fitapet.domain.domains.care.domain.QCareCategory;
import kr.co.fitapet.domain.domains.pet.domain.QPet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import static com.querydsl.core.types.dsl.Expressions.constant;

@Repository
@Slf4j
@RequiredArgsConstructor
public class CareQueryDslRepositoryImpl implements CareQueryDslRepository {
    private final JPAQueryFactory queryFactory;
    private final QPet pet = QPet.pet;
    private final QCareCategory careCategory = QCareCategory.careCategory;
    private final QCare care = QCare.care;

    public boolean isValidCare(Long petId, Long careId) {
        return queryFactory
                .select(constant(1))
                .from(pet)
                .leftJoin(careCategory).on(careCategory.pet.id.eq(pet.id))
                .leftJoin(care).on(care.careCategory.id.eq(careCategory.id))
                .where(pet.id.eq(petId).and(care.id.eq(careId)))
                .fetchOne() != null;
    }
}
