package kr.co.fitapet.domain.domains.care.service;

import jakarta.transaction.Transactional;
import kr.co.fitapet.common.annotation.DomainService;
import kr.co.fitapet.domain.domains.care.domain.Care;
import kr.co.fitapet.domain.domains.care.domain.CareCategory;
import kr.co.fitapet.domain.domains.care.domain.CareDate;
import kr.co.fitapet.domain.domains.care.repository.CareCategoryRepository;
import kr.co.fitapet.domain.domains.care.repository.CareDateRepository;
import kr.co.fitapet.domain.domains.care.repository.CareRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@DomainService
@RequiredArgsConstructor
@Slf4j
public class CareSaveService {
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
