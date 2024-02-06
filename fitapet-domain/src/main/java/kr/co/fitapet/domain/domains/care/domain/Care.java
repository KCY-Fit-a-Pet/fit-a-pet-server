package kr.co.fitapet.domain.domains.care.domain;

import jakarta.persistence.*;
import kr.co.fitapet.domain.common.model.AuthorAuditable;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CARE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"careName"})
@Getter
public class Care extends AuthorAuditable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "care_name")
    private String careName;
    @Column(name = "limit_time")
    private Integer limitTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "care_category_id")
    private CareCategory careCategory;

    @OneToMany(mappedBy = "care", cascade = CascadeType.ALL)
    private List<CareDate> careDates = new ArrayList<>();

    @Builder
    private Care(String careName, Integer limitTime, CareCategory careCategory) {
        this.careName = careName;
        this.limitTime = limitTime;
        this.careCategory = careCategory;
    }

    public static Care of(String careName, Integer limitTime, CareCategory careCategory) {
        return Care.builder()
                .careName(careName)
                .limitTime(limitTime)
                .careCategory(careCategory)
                .build();
    }

    public void updateCareCategory(CareCategory careCategory) {
        if (this.careCategory != null) {
            this.careCategory.getCares().remove(this);
        }
        this.careCategory = careCategory;
        careCategory.getCares().add(this);
    }
}
