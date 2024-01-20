package com.kcy.fitapet.domain.schedule.domain;

import com.kcy.fitapet.domain.member.domain.Member;
import com.kcy.fitapet.domain.model.AuthorAuditable;
import com.kcy.fitapet.domain.model.DateAuditable;
import com.kcy.fitapet.domain.pet.domain.PetSchedule;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "SCHEDULE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@ToString(of = {"scheduleName", "location", "reservationDt", "notifyDt", "isDone"})
public class Schedule extends AuthorAuditable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "schedule_name")
    private String scheduleName;
    private String location;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "reservation_dt")
    private LocalDateTime reservationDt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "notify_dt")
    private LocalDateTime notifyDt;

    @Column(name = "is_done") @ColumnDefault("false")
    private boolean isDone = false;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
    private List<PetSchedule> pets = new ArrayList<>();

    @Builder
    private Schedule(String scheduleName, String location, LocalDateTime reservationDt, LocalDateTime notifyDt) {
        this.scheduleName = scheduleName;
        this.location = location;
        this.reservationDt = reservationDt;
        this.notifyDt = notifyDt;
    }

    public static Schedule of(String scheduleName, String location, LocalDateTime reservationDt, LocalDateTime notifyDt) {
        return Schedule.builder()
                .scheduleName(scheduleName)
                .location(location)
                .reservationDt(reservationDt)
                .notifyDt(notifyDt)
                .build();
    }

    public void updateState() {
        this.isDone = !this.isDone;
    }
}
