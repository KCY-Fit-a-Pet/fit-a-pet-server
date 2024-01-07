package com.kcy.fitapet.domain.pet.domain;

import com.kcy.fitapet.domain.care.domain.Care;
import com.kcy.fitapet.domain.model.DateAuditable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PET_CARE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PetCare extends DateAuditable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "care_id")
    private Care care;

    private PetCare(Pet pet, Care care) {
        this.pet = pet;
        this.care = care;
    }

    public static PetCare of(Pet pet, Care care) {
        return new PetCare(pet, care);
    }

    public void updatePet(Pet pet) {
        if (this.pet != null) {
            this.pet.getCares().remove(this);
        }

        this.pet = pet;

        if (pet != null) {
            pet.getCares().add(this);
        }
    }

    public void updateCare(Care care) {
        if (this.care != null) {
            this.care.getPets().remove(this);
        }

        this.care = care;

        if (care != null) {
            care.getPets().add(this);
        }
    }
}
