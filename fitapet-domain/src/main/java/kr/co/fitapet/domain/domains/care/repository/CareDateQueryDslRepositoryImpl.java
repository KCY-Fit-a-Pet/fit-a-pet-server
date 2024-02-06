package kr.co.fitapet.domain.domains.care.repository;

import kr.co.fitapet.domain.domains.care.domain.QCare;
import kr.co.fitapet.domain.domains.care.domain.QCareDate;
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
