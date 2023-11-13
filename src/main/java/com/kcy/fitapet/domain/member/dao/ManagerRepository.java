package com.kcy.fitapet.domain.member.dao;

import com.kcy.fitapet.domain.member.domain.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerRepository extends JpaRepository<Manager, Long> {
}
