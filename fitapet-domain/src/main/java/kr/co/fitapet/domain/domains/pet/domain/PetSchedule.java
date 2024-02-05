package kr.co.fitapet.domain.domains.pet.domain;

import com.kcy.fitapet.domain.schedule.domain.Schedule;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "PET_SCHEDULE")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@EntityListeners(AuditingEntityListener.class)
public class PetSchedule {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @CreatedDate
    @Column(name = "joined_at", nullable = false, updatable = false)
    private LocalDateTime joinedAt;

    public static PetSchedule of(Pet pet, Schedule schedule) {
        PetSchedule petSchedule = new PetSchedule();
        petSchedule.mappingPetAndSchedule(pet, schedule);
        return petSchedule;
    }

    public void mappingPetAndSchedule(Pet pet, Schedule schedule) {
        updatePet(pet);
        updateSchedule(schedule);
    }

    private void updatePet(Pet pet) {
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
}
