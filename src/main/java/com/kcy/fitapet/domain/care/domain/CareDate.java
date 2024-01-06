package com.kcy.fitapet.domain.care.domain;

import com.kcy.fitapet.domain.care.type.WeekType;
import com.kcy.fitapet.domain.care.type.WeekTypeConverter;
import com.kcy.fitapet.domain.model.Auditable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Table(name = "CARE_DETAIL")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CareDate extends Auditable {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "care_detail_name")
    private String careDetailName;
    @Convert(converter = WeekTypeConverter.class)
    private WeekType week;
    @Temporal(TemporalType.TIME)
    @Column(name = "care_time")
    private LocalTime careTime;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "care_id")
    private Care care;

    @Builder
    private CareDate(WeekType week, LocalTime careTime) {
        this.week = week;
        this.careTime = careTime;
    }

    public static CareDate of(WeekType week, LocalTime careTime) {
        return CareDate.builder()
                .week(week)
                .careTime(careTime)
                .build();
    }

    @Override public String toString() {
        return "CareDetail{" +
                "id=" + id +
                ", week='" + week + '\'' +
                ", careTime=" + careTime +
                '}';
    }
}
