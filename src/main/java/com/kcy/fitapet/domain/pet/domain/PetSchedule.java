package com.kcy.fitapet.domain.pet.domain;

import com.kcy.fitapet.domain.model.DateAuditable;
import com.kcy.fitapet.domain.schedule.domain.Schedule;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PET_SCHEDULE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PetSchedule extends DateAuditable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    public void updatePet(Pet pet) {
        if (this.pet != null) {
            this.pet.getSchedules().remove(this);
        }

        this.pet = pet;
        pet.getSchedules().add(this);
    }

    public void updateSchedule(Schedule schedule) {
        if (this.schedule != null) {
            this.schedule.getPets().remove(this);
        }

        this.schedule = schedule;
        schedule.getPets().add(this);
    }

    public void mappingPetAndSchedule(Pet pet, Schedule schedule) {
        updatePet(pet);
        updateSchedule(schedule);
    }
}
