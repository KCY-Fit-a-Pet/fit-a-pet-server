package kr.co.fitapet.domain.domains.care.domain;

import jakarta.persistence.*;
import kr.co.fitapet.domain.common.model.DateAuditable;
import kr.co.fitapet.domain.domains.pet.domain.Pet;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CARE_CATEGORY")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(of = {"id", "categoryName"})
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
