package kr.co.fitapet.domain.domains.member.repository;


import kr.co.fitapet.domain.common.repository.ExtendedRepository;
import kr.co.fitapet.domain.domains.member.domain.Member;

import java.util.Optional;

public interface MemberRepository extends ExtendedRepository<Member, Long>, MemberQueryDslRepository {
    Optional<Member> findByUid(String uid);
    Optional<Member> findByPhone(String phone);
    boolean existsByUidOrPhone(String uid, String phone);
    boolean existsByPhone(String phone);
    boolean existsByUid(String uid);
    boolean existsByPhoneAndUid(String phone, String uid);
}
