package kr.co.fitapet.domain.domains.manager.domain;

import kr.co.fitapet.domain.common.model.DateAuditable;
import jakarta.persistence.*;
import kr.co.fitapet.domain.domains.member.domain.Member;
import kr.co.fitapet.domain.domains.member.type.ManageType;
import kr.co.fitapet.domain.common.converter.ManageTypeConverter;
import kr.co.fitapet.domain.domains.pet.domain.Pet;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Table(name = "MANAGER")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Manager extends DateAuditable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ColumnDefault("false")
    private boolean hidden;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

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

    private Manager(Member member, Pet pet, boolean hidden, ManageType manageType) {
        this.member = member;
        this.pet = pet;
        this.hidden = hidden;
        this.manageType = manageType;
    }

    public static Manager of(Member manager, Pet pet, boolean hidden, ManageType manageType) {
        return new Manager(manager, pet, hidden, manageType);
    }

    public void updateManager(Member manager) {
        if (this.member != null) {
            this.member.getUnderCares().remove(this);
        }

        this.member = manager;

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
