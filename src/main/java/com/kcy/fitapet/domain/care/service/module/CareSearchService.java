package com.kcy.fitapet.domain.care.service.module;

import com.kcy.fitapet.domain.care.dao.CareCategoryRepository;
import com.kcy.fitapet.domain.care.dao.CareDateRepository;
import com.kcy.fitapet.domain.care.dao.CareRepository;
import com.kcy.fitapet.domain.care.domain.CareCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CareSearchService {
    private final CareRepository careRepository;
    private final CareCategoryRepository careCategoryRepository;
    private final CareDateRepository careDateRepository;

    @Transactional(readOnly = true)
    public CareCategory findCareCategoryById(Long categoryId) {
        return careCategoryRepository.findByIdOrElseThrow(categoryId);
    }
}
