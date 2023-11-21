package com.kcy.fitapet.domain.care.domain;

import com.kcy.fitapet.domain.care.type.WeekType;
import com.kcy.fitapet.domain.care.type.CareTypeConverter;
import com.kcy.fitapet.domain.member.domain.Member;
import com.kcy.fitapet.domain.model.Auditable;
import com.kcy.fitapet.domain.pet.domain.PetCare;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CARE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"careName", "dtype"})
@Getter
public class Care extends Auditable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "care_name")
    private String careName;
    @Column(name = "week")
    @Convert(converter = CareTypeConverter.class)
    private WeekType week;
    @Column(name = "limit_time")
    @Temporal(TemporalType.TIME)
    private LocalTime limitTime;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", updatable = false)
    private Member author;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_editor_id")
    private Member lastEditor;
    @OneToMany(mappedBy = "care", cascade = CascadeType.ALL)
    private List<PetCare> pets = new ArrayList<>();
    @OneToMany(mappedBy = "care", cascade = CascadeType.ALL)
    private List<CareDetail> careDetails = new ArrayList<>();

    @Builder
    private Care(String careName, WeekType week, LocalTime limitTime) {
        this.careName = careName;
        this.week = week;
        this.limitTime = limitTime;
    }

    public static Care of(String careName, WeekType week) {
        return Care.builder()
                .careName(careName)
                .week(week)
                .build();
    }
}
