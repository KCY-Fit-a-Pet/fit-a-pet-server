package com.kcy.fitapet.domain.care.domain;

import com.kcy.fitapet.domain.model.Auditable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CARE_CATEGORY")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CareCategory extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String categoryName;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Care> cares = new ArrayList<>();

    private CareCategory(String categoryName) {
        this.categoryName = categoryName;
    }

    public static CareCategory of(String categoryName) {
        return new CareCategory(categoryName);
    }
}
