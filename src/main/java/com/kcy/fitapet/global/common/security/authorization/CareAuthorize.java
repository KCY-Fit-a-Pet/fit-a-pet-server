package com.kcy.fitapet.global.common.security.authorization;

import com.kcy.fitapet.domain.care.dao.CareDateRepository;
import com.kcy.fitapet.domain.care.dao.CareRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component("careAuthorize")
@RequiredArgsConstructor
@Slf4j
public class CareAuthorize {
    private final CareRepository careRepository;
    private final CareDateRepository careDateRepository;

    public boolean isValidCare(Long petId, Long careId) {
        return careRepository.isValidCare(petId, careId);
    }

    public boolean isValidCareAndCareDate(Long petId, Long careId, Long careDateId) {
        return isValidCare(petId, careId) && isValidCareDate(careId, careDateId);
    }

    private boolean isValidCareDate(Long careId, Long careDateId) {
        return careDateRepository.existsByIdAndCare_Id(careDateId, careId);
    }
}
