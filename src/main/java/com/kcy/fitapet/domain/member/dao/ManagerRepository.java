package com.kcy.fitapet.domain.member.dao;

import com.kcy.fitapet.domain.member.domain.Manager;
import com.kcy.fitapet.global.common.repository.ExtendedRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ManagerRepository extends ExtendedRepository<Manager, Long> {
    boolean existsByMember_IdAndPet_Id(Long memberId, Long petId);
    List<Manager> findAllByMember_Id(Long memberId);
}
