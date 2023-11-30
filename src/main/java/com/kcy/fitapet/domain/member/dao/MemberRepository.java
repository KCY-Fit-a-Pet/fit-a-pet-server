package com.kcy.fitapet.domain.member.dao;

import com.kcy.fitapet.domain.member.domain.Member;
import com.kcy.fitapet.domain.member.dto.mapping.MemberUidMapping;
import com.kcy.fitapet.global.common.repository.ExtendedRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends ExtendedRepository<Member, Long> {
    Optional<Member> findByUid(String uid);
    boolean existsByUidOrEmailOrPhone(String uid, String email, String phone);
    boolean existsByPhone(String phone);

    @Query("SELECT m.uid, m.createdAt FROM Member m WHERE m.phone = :phone")
    Optional<MemberUidMapping> findUidAndCreatedAtByPhone(String phone);
}
