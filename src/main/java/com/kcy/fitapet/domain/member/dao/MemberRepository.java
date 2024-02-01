package com.kcy.fitapet.domain.member.dao;

import com.kcy.fitapet.domain.member.domain.Member;
import com.kcy.fitapet.global.common.repository.ExtendedRepository;

import java.util.Optional;

public interface MemberRepository extends ExtendedRepository<Member, Long>, MemberQueryDslRepository {
    Optional<Member> findByUid(String uid);
    Optional<Member> findByPhone(String phone);
    boolean existsByUidOrPhone(String uid, String phone);
    boolean existsByPhone(String phone);
    boolean existsByUid(String uid);
    boolean existsByPhoneAndUid(String phone, String uid);
}
