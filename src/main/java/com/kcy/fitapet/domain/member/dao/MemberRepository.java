package com.kcy.fitapet.domain.member.dao;

import com.kcy.fitapet.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUid(String uid);
    boolean existsByUidOrEmailOrPhone(String uid, String email, String phone);
    boolean existsByPhone(String phone);
}
