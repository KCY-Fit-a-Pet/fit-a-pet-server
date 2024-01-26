package com.kcy.fitapet.domain.care.service.module;

import com.kcy.fitapet.domain.care.dao.CareCategoryJpaRepository;
import com.kcy.fitapet.domain.care.dao.CareDateJpaRepository;
import com.kcy.fitapet.domain.care.dao.CareJpaRepository;
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
public class CareSaveService {
    private final CareJpaRepository careRepository;
    private final CareDateJpaRepository careDateRepository;
    private final CareCategoryJpaRepository careCategoryRepository;

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
