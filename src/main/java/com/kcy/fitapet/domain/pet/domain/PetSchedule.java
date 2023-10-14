package com.kcy.fitapet.domain.pet.domain;

import com.kcy.fitapet.domain.model.Auditable;
import com.kcy.fitapet.domain.schedule.domain.Schedule;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "PET_SCHEDULE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PetSchedule extends Auditable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;
}
