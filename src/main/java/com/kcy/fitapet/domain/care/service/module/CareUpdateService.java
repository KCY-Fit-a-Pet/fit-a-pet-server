package com.kcy.fitapet.domain.care.service.module;

import com.kcy.fitapet.domain.care.dao.CareCategoryRepository;
import com.kcy.fitapet.domain.care.dao.CareDateRepository;
import com.kcy.fitapet.domain.care.dao.CareRepository;
import com.kcy.fitapet.domain.care.domain.Care;
import com.kcy.fitapet.domain.care.domain.CareCategory;
import com.kcy.fitapet.domain.care.domain.CareDate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CareUpdateService {
    private final CareRepository careRepository;
    private final CareDateRepository careDateRepository;
    private final CareCategoryRepository careCategoryRepository;

    @Transactional
    public void saveCare(Care care) {
        careRepository.save(care);
    }

    @Transactional
    public void saveCareCategories(List<CareCategory> careCategories) {
        careCategoryRepository.saveAll(careCategories);
    }

    @Transactional
    public void saveCares(List<Care> cares) {
        careRepository.saveAll(cares);
    }

    @Transactional
    public void saveCareDates(List<CareDate> careDates) {
        careDateRepository.saveAll(careDates);
    }

    @Transactional
    public void saveCareDate(CareDate careDate) {
        careDateRepository.save(careDate);
    }

    @Transactional
    public void saveCareDateList(List<CareDate> careDateList) {
        careDateRepository.saveAll(careDateList);
    }

    @Transactional
    public CareCategory saveCareCategory(CareCategory careCategory) {
        return careCategoryRepository.save(careCategory);
    }
}
