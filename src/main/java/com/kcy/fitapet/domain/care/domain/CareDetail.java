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
import java.util.List;

@Entity
@Table(name = "CARE_DETAIL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CareDetail extends Auditable {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "care_detail_name")
    private String careDetailName;
    @Temporal(TemporalType.TIME)
    @Column(name = "care_time")
    private LocalTime careTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "care_id")
    private Care care;

    @OneToMany(mappedBy = "careDetail")
    private List<CareLog> careLogs;

    @Builder
    private CareDetail(String careDetailName, LocalTime careTime) {
        this.careDetailName = careDetailName;
        this.careTime = careTime;
    }

    public static CareDetail of(String careDetailName, LocalTime careTime, LocalTime limitTime, Boolean isDone, LocalDateTime clearedAt) {
        return CareDetail.builder()
                .careDetailName(careDetailName)
                .careTime(careTime)
                .build();
    }

    @Override public String toString() {
        return "CareDetail{" +
                "id=" + id +
                ", careDetailName='" + careDetailName + '\'' +
                ", careTime=" + careTime +
                '}';
    }
}
