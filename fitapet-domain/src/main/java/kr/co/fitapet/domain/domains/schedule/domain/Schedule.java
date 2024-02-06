package kr.co.fitapet.domain.domains.schedule.domain;

import jakarta.persistence.*;
import kr.co.fitapet.domain.common.model.AuthorAuditable;
import kr.co.fitapet.domain.domains.pet.domain.PetSchedule;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "SCHEDULE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"scheduleName", "location", "reservationDt", "notifyDt"})
public class Schedule extends AuthorAuditable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "reservation_dt")
    private LocalDateTime reservationDt;

    @Column(name = "schedule_name")
    private String scheduleName;
    private String location;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "notify_dt")
    private LocalDateTime notifyDt;

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
}
