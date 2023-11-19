package com.kcy.fitapet.domain.member.domain;

import com.kcy.fitapet.domain.member.type.ManageType;
import com.kcy.fitapet.domain.member.type.ManageTypeConverter;
import com.kcy.fitapet.domain.model.Auditable;
import com.kcy.fitapet.domain.pet.domain.Pet;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Table(name = "MANAGER")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Manager extends Auditable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ColumnDefault("false")
    private boolean hidden;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member manager;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @Column(name = "mtype")
    @Convert(converter = ManageTypeConverter.class)
    private ManageType manageType;

    public void hide() {
        this.hidden = true;
    }

    public void show() {
        this.hidden = false;
    }

    private Manager(Member manager, Pet pet, boolean hidden, ManageType manageType) {
        this.manager = manager;
        this.pet = pet;
        this.hidden = hidden;
        this.manageType = manageType;
    }

    public static Manager of(Member manager, Pet pet, boolean hidden, ManageType manageType) {
        return new Manager(manager, pet, hidden, manageType);
    }

    public void updateManager(Member manager) {
        if (this.manager != null) {
            this.manager.getUnderCares().remove(this);
        }

        this.manager = manager;

        if (manager != null) {
            manager.getUnderCares().add(this);
        }
    }

    public void updatePet(Pet pet) {
        if (this.pet != null) {
            this.pet.getManagers().remove(this);
        }

        this.pet = pet;

        if (pet != null) {
            pet.getManagers().add(this);
        }
    }
}
