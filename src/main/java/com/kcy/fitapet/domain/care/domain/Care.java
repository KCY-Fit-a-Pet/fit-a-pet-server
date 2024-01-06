package com.kcy.fitapet.domain.care.domain;

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
@ToString(of = {"careName"})
@Getter
public class Care extends Auditable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "care_name")
    private String careName;
    @Column(name = "limit_time")
    @Temporal(TemporalType.TIME)
    private LocalTime limitTime;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", updatable = false)
    private Member author;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_editor_id")
    private Member lastEditor;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CareCategory category;

    @OneToMany(mappedBy = "care", cascade = CascadeType.ALL)
    private List<PetCare> pets = new ArrayList<>();
    @OneToMany(mappedBy = "care", cascade = CascadeType.ALL)
    private List<CareDate> careDates = new ArrayList<>();

    @Builder
    private Care(String careName, LocalTime limitTime, Member author, CareCategory category) {
        this.careName = careName;
        this.limitTime = limitTime;
        this.author = author;
        this.category = category;
    }

    public static Care of(String careName, LocalTime limitTime, Member author, CareCategory category) {
        return Care.builder()
                .careName(careName)
                .limitTime(limitTime)
                .author(author)
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
