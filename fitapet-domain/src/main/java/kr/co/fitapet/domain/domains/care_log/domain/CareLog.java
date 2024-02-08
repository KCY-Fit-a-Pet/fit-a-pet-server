package kr.co.fitapet.domain.domains.care_log.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import kr.co.fitapet.domain.domains.care.domain.CareDate;
import kr.co.fitapet.domain.domains.member.domain.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "CARE_LOG")
@IdClass(CareLogId.class)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class CareLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Id @CreatedDate
    private LocalDateTime logDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "care_date_id", nullable = false, updatable = false)
    private CareDate careDate;

    @CreatedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false, updatable = false)
    @JsonIgnore
    private Member author;

    public static CareLog of(CareDate careDate) {
        CareLog careLog = new CareLog();
        careLog.setCareDate(careDate);
        return careLog;
    }

    public void setCareDate(CareDate careDate) {
        if (this.careDate != null) {
            this.careDate.getCareLogs().remove(this);
        }

        this.careDate = careDate;
        careDate.getCareLogs().add(this);
    }
}
