package com.kcy.fitapet.domain.care.domain;

import com.kcy.fitapet.domain.member.domain.Member;
import com.kcy.fitapet.domain.model.DateAuditable;
import com.kcy.fitapet.domain.pet.domain.PetCare;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CARE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"careName"})
@Getter
public class Care extends DateAuditable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "care_name")
    private String careName;
    @Column(name = "limit_time")
    private Integer limitTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CareCategory category;

    @OneToMany(mappedBy = "care", cascade = CascadeType.ALL)
    private List<PetCare> pets = new ArrayList<>();
    @OneToMany(mappedBy = "care", cascade = CascadeType.ALL)
    private List<CareDate> careDates = new ArrayList<>();

    @Builder
    private Care(String careName, Integer limitTime, CareCategory category) {
        this.careName = careName;
        this.limitTime = limitTime;
        this.category = category;
    }

    public static Care of(String careName, Integer limitTime, CareCategory category) {
        return Care.builder()
                .careName(careName)
                .limitTime(limitTime)
                .category(category)
                .build();
    }

    public void updateCategory(CareCategory category) {
        if (this.category != null) {
            this.category.getCares().remove(this);
        }
        this.category = category;
        category.getCares().add(this);
    }
}
