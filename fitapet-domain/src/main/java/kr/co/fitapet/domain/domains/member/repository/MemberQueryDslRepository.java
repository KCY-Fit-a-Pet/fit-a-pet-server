package kr.co.fitapet.domain.domains.member.repository;

import kr.co.fitapet.domain.domains.member.domain.Member;
import kr.co.fitapet.domain.domains.member.dto.MemberInfo;

import java.util.List;
import java.util.Optional;

public interface MemberQueryDslRepository {
    List<Member> findByIds(List<Long> ids);
    List<Long> findMyPetIds(Long memberId);
    Optional<MemberInfo> findMemberInfo(Long requesterId, String target);
    List<MemberInfo> findMemberInfos(List<Long> memberIds, Long requesterId);
}
