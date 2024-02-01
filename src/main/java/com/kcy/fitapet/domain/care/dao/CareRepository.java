package com.kcy.fitapet.domain.care.dao;

import com.kcy.fitapet.domain.care.domain.Care;
import com.kcy.fitapet.global.common.repository.ExtendedJpaRepository;
import com.kcy.fitapet.global.common.repository.ExtendedRepository;

public interface CareRepository extends ExtendedRepository<Care, Long>, CareQueryDslRepository {

}
