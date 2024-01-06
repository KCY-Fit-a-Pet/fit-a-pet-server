package com.kcy.fitapet.domain.log.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "CARE_LOG")
@IdClass(CareLogId.class)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CareLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Id @CreatedDate
    private LocalDateTime logDate;

    private Long careDateId;

    private CareLog(Long careDateId) {
        this.careDateId = careDateId;
    }

    public static CareLog of(Long careDateId) {
        return new CareLog(careDateId);
    }
}
