package com.kcy.fitapet.domain.care.domain;

import com.kcy.fitapet.domain.model.Auditable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "CARE_DETAIL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"careDetailName", "careTime", "limitTime", "isDone", "clearedAt"})
public class CareDetail extends Auditable {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "care_detail_name")
    private String careDetailName;
    @Temporal(TemporalType.TIME)
    @Column(name = "care_time")
    private LocalTime careTime;
    @Temporal(TemporalType.TIME)
    @Column(name = "limit_time")
    private LocalTime limitTime;
    @Column(name = "is_done") @ColumnDefault("false")
    private Boolean isDone;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "cleared_at")
    private LocalDateTime clearedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "care_id")
    private Care care;
    @OneToOne(mappedBy = "careDetail")
    private DayOfWeek dayOfWeek;

    @Builder
    private CareDetail(String careDetailName, LocalTime careTime, LocalTime limitTime, Boolean isDone, LocalDateTime clearedAt) {
        this.careDetailName = careDetailName;
        this.careTime = careTime;
        this.limitTime = limitTime;
        this.isDone = isDone;
        this.clearedAt = clearedAt;
    }

    public static CareDetail of(String careDetailName, LocalTime careTime, LocalTime limitTime, Boolean isDone, LocalDateTime clearedAt) {
        return CareDetail.builder()
                .careDetailName(careDetailName)
                .careTime(careTime)
                .limitTime(limitTime)
                .isDone(isDone)
                .clearedAt(clearedAt)
                .build();
    }

    public void isDone() {
        this.isDone = Boolean.TRUE;
        this.clearedAt = LocalDateTime.now();
    }

    public void cancelDone() {
        this.isDone = Boolean.FALSE;
        this.clearedAt = null;
    }
}