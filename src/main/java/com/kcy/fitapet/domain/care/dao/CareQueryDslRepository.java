package com.kcy.fitapet.domain.care.dao;

public interface CareQueryDslRepository {
    boolean isValidCare(Long petId, Long careId);
}
