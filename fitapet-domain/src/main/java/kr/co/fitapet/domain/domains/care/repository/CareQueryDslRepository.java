package kr.co.fitapet.domain.domains.care.repository;

public interface CareQueryDslRepository {
    boolean isValidCare(Long petId, Long careId);
}
