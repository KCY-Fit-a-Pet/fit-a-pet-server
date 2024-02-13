package kr.co.fitapet.domain.domains.member.repository;

import kr.co.fitapet.domain.domains.member.domain.Member;

import java.util.List;

public interface MemberQueryDslRepository {
    List<Long> findMyPetIds(Long memberId);

}
