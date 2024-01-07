package com.kcy.fitapet.domain.care.domain;

import com.kcy.fitapet.domain.care.type.WeekType;
import com.kcy.fitapet.domain.care.type.WeekTypeConverter;
import com.kcy.fitapet.domain.model.DateAuditable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Table(name = "CARE_DATE")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CareDate extends DateAuditable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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

    public void updateCare(Care care) {
        if (this.care != null) {
            this.care.getCareDates().remove(this);
        }
        this.care = care;
        care.getCareDates().add(this);
    }

    @Override public String toString() {
        return "CareDetail{" +
                "id=" + id +
                ", week='" + week + '\'' +
                ", careTime=" + careTime +
                '}';
    }
}
