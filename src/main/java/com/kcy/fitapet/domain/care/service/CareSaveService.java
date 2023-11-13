package com.kcy.fitapet.domain.care.service;

import com.kcy.fitapet.domain.care.dao.CareRepository;
import com.kcy.fitapet.domain.care.domain.Care;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CareSaveService {
    private final CareRepository careRepository;

    @Transactional
    public Care saveCare(Care care) {
        return careRepository.save(care);
    }

    @Transactional
    public List<Care> saveCares(List<Care> cares) {
        return cares.stream()
                .map(careRepository::save)
                .toList();
    }
}
