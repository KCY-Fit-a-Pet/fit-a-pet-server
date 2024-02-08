package kr.co.fitapet.domain.domains.member.repository;

import java.util.List;

public interface MemberQueryDslRepository {
    List<Long> findMyPetIds(Long memberId);
}
