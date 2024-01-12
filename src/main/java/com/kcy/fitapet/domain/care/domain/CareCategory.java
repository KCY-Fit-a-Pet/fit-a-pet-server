package com.kcy.fitapet.domain.care.domain;

import com.kcy.fitapet.domain.model.DateAuditable;
import com.kcy.fitapet.domain.pet.domain.Pet;
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
public class CareCategory extends DateAuditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String categoryName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @OneToMany(mappedBy = "careCategory", cascade = CascadeType.ALL)
    private List<Care> cares = new ArrayList<>();

    private CareCategory(String categoryName) {
        this.categoryName = categoryName;
    }

    public static CareCategory of(String categoryName) {
        return new CareCategory(categoryName);
    }

    public void updatePet(Pet pet) {
        if (this.pet != null) {
            this.pet.getCareCategories().remove(this);
        }
        this.pet = pet;
        pet.getCareCategories().add(this);
    }
}
