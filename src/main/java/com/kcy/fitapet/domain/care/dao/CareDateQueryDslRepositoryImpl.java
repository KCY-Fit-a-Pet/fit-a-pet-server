package com.kcy.fitapet.domain.care.dao;

import com.kcy.fitapet.domain.care.domain.QCare;
import com.kcy.fitapet.domain.care.domain.QCareDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CareDateQueryDslRepositoryImpl implements CareDateQueryDslRepository {
    private final QCare care = QCare.care;
    private final QCareDate careDate = QCareDate.careDate;

}
