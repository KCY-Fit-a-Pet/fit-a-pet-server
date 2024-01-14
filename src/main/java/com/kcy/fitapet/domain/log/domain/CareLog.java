package com.kcy.fitapet.domain.log.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kcy.fitapet.domain.care.domain.CareDate;
import com.kcy.fitapet.domain.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "CARE_LOG")
@IdClass(CareLogId.class)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class CareLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Id @CreatedDate
    private LocalDateTime logDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "care_date_id", nullable = false, updatable = false)
    private CareDate careDate;

    @CreatedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false, updatable = false)
    @JsonIgnore
    private Member author;
}
