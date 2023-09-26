package com.kcy.fitapet.domain.care.domain;

import com.kcy.fitapet.domain.model.Auditable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "DAY_OF_WEEK")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(exclude = {"id", "careDetail"})
public class DayOfWeek extends Auditable {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ColumnDefault("false")
    private boolean mon;
    @ColumnDefault("false")
    private boolean tue;
    @ColumnDefault("false")
    private boolean wed;
    @ColumnDefault("false")
    private boolean thu;
    @ColumnDefault("false")
    private boolean fri;
    @ColumnDefault("false")
    private boolean sat;
    @ColumnDefault("false")
    private boolean sun;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "care_detail_id")
    private CareDetail careDetail;

    @Builder
    private DayOfWeek(boolean mon, boolean tue, boolean wed, boolean thu, boolean fri, boolean sat, boolean sun, CareDetail careDetail) {
        this.mon = mon;
        this.tue = tue;
        this.wed = wed;
        this.thu = thu;
        this.fri = fri;
        this.sat = sat;
        this.sun = sun;
        this.careDetail = careDetail;
    }

    public static DayOfWeek of(boolean mon, boolean tue, boolean wed, boolean thu, boolean fri, boolean sat, boolean sun, CareDetail careDetail) {
        return DayOfWeek.builder()
                .mon(mon).tue(tue).wed(wed).thu(thu).fri(fri).sat(sat).sun(sun).careDetail(careDetail).build();
    }
}
