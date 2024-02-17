package kr.co.fitapet.domain.domains.member.repository;

import kr.co.fitapet.domain.common.repository.ExtendedRepository;
import kr.co.fitapet.domain.domains.member.domain.MemberNickname;

import java.util.Optional;

public interface MemberNicknameRepository extends ExtendedRepository<MemberNickname, Long> {
    boolean existsByFrom_IdAndTo_Id(Long fromId, Long toId);
    Optional<MemberNickname> findByFrom_IdAndTo_Id(Long fromId, Long toId);
}
