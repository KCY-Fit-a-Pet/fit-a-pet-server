package com.kcy.fitapet.domain.care.domain;

import com.kcy.fitapet.domain.member.domain.Member;
import com.kcy.fitapet.domain.model.Auditable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CARE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"careName", "dtype"})
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
    private List<CareDetail> careDetails = new ArrayList<>();

    @Builder
    private Care(String careName, CareType dtype, Member author, Member lastEditor, List<CareDetail> careDetails) {
        this.careName = careName;
        this.dtype = dtype;
        this.author = author;
        this.lastEditor = lastEditor;
        this.careDetails = careDetails;
    }

    public static Care of(String careName, CareType dtype, Member author, Member lastEditor, List<CareDetail> careDetails) {
        return Care.builder()
                .careName(careName)
                .dtype(dtype)
                .author(author)
                .lastEditor(lastEditor)
                .careDetails(careDetails)
                .build();
    }
}
