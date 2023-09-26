package com.kcy.fitapet.domain.member.dao;

import com.kcy.fitapet.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUid(String uid);
    boolean existsByUid(String uid);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
}
