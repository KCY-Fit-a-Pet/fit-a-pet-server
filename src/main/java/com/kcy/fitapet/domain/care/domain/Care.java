package com.kcy.fitapet.domain.care.domain;

import com.kcy.fitapet.domain.care.type.CareType;
import com.kcy.fitapet.domain.care.type.CareTypeConverter;
import com.kcy.fitapet.domain.member.domain.Member;
import com.kcy.fitapet.domain.model.Auditable;
import com.kcy.fitapet.domain.pet.domain.PetCare;
import jakarta.persistence.*;
import lombok.*;

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
    @Column(name = "dtype")
    @Convert(converter = CareTypeConverter.class)
    private CareType dtype;

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
    private Care(String careName, CareType dtype) {
        this.careName = careName;
        this.dtype = dtype;
    }

    public static Care of(String careName, CareType dtype) {
        return Care.builder()
                .careName(careName)
                .dtype(dtype)
                .build();
    }
}
