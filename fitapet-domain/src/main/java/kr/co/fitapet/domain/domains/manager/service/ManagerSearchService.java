package kr.co.fitapet.domain.domains.manager.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import kr.co.fitapet.common.annotation.DomainService;
import kr.co.fitapet.domain.common.repository.QueryHandler;
import kr.co.fitapet.domain.domains.manager.domain.Manager;
import kr.co.fitapet.domain.domains.manager.domain.QManager;
import kr.co.fitapet.domain.domains.manager.repository.ManagerRepository;
import kr.co.fitapet.domain.domains.member.domain.Member;
import kr.co.fitapet.domain.domains.member.domain.QMember;
import kr.co.fitapet.domain.domains.member.repository.MemberRepository;
import kr.co.fitapet.domain.domains.pet.domain.QPet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@DomainService
@RequiredArgsConstructor
public class ManagerSearchService {
    private final MemberRepository memberRepository;
    private final ManagerRepository managerRepository;

    @Transactional(readOnly = true)
    public List<Manager> findAllManagerByMemberId(Long memberId) {
        return managerRepository.findAllByMember_Id(memberId);
    }

    @Transactional(readOnly = true)
    public boolean isManager(Long memberId, Long petId) {
        return managerRepository.existsByMember_IdAndPet_Id(memberId, petId);
    }

    @Transactional(readOnly = true)
    public boolean isManagerAll(Long memberId, List<Long> petIds) {
        if (petIds.isEmpty()) {
            return true;
        }

        for (Long petId : petIds) {
            if (!isManager(memberId, petId)) {
                return false;
            }
        }
        return true;
    }

    @Transactional(readOnly = true)
    public Long findMasterIdByPetId(Long petId) {
        return managerRepository.findMasterIdByPetId(petId);
    }

    @Transactional(readOnly = true)
    public List<Member> findAllManagerByPetId(Long petId) {
        final QMember member = QMember.member;
        final QManager manager = QManager.manager;
        final QPet pet = QPet.pet;

        QueryHandler queryHandler = query -> query.leftJoin(manager).on(manager.member.id.eq(member.id))
                .leftJoin(pet).on(manager.pet.id.eq(pet.id));
        Predicate predicate = new BooleanBuilder().and(pet.id.eq(petId));
        return memberRepository.findList(predicate, queryHandler, null);
    }
}
