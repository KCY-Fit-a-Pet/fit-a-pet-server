package com.kcy.fitapet.domain.schedule.domain;

import com.kcy.fitapet.domain.member.domain.Member;
import com.kcy.fitapet.domain.model.DateAuditable;
import com.kcy.fitapet.domain.pet.domain.PetSchedule;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "SCHEDULE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"scheduleName", "location", "reservationDt", "notifyDt", "isDone"})
public class Schedule extends DateAuditable {
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
    private boolean isDone;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
    private List<PetSchedule> pets = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", updatable = false)
    private Member author;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_editor_id")
    private Member lastEditor;

    @Builder
    private Schedule(String scheduleName, String location, LocalDateTime reservationDt, LocalDateTime notifyDt, boolean isDone) {
        this.scheduleName = scheduleName;
        this.location = location;
        this.reservationDt = reservationDt;
        this.notifyDt = notifyDt;
        this.isDone = isDone;
    }

    public static Schedule of(String scheduleName, String location, LocalDateTime reservationDt, LocalDateTime notifyDt, boolean isDone) {
        return Schedule.builder()
                .scheduleName(scheduleName)
                .location(location)
                .reservationDt(reservationDt)
                .notifyDt(notifyDt)
                .isDone(isDone)
                .build();
    }
}
