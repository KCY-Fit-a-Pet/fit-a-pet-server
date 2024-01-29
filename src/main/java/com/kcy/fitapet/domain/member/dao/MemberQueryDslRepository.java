package com.kcy.fitapet.domain.member.dao;

import java.util.List;

public interface MemberQueryDslRepository {
    List<Long> findMyPetIds(Long memberId);
}
