package kr.co.fitapet.domain.domains.invitation.domain;

import jakarta.persistence.*;
import kr.co.fitapet.domain.common.model.DateAuditable;
import kr.co.fitapet.domain.domains.member.domain.Member;
import kr.co.fitapet.domain.domains.pet.domain.Pet;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedBy;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "MANAGER_INVITATION")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@Getter
@ToString(of = {"id", "expireDate", "isAccepted"})
public class ManagerInvitation extends DateAuditable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime expireDate;
    @ColumnDefault("false")
    private Boolean isAccepted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_id")
    @CreatedBy
    private Member from;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_id")
    private Member to;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @Builder
    private ManagerInvitation(Member from, Member to, LocalDateTime expireDate, Pet pet, Boolean isAccepted) {
        this.from = from;
        this.to = to;
        this.expireDate = expireDate;
        this.pet = pet;
        this.isAccepted = isAccepted;
    }

    public static ManagerInvitation of(Member from, Member to, LocalDateTime expireDate, Pet pet) {
        return ManagerInvitation.builder()
                .from(from)
                .to(to)
                .expireDate(expireDate)
                .pet(pet)
                .isAccepted(Boolean.FALSE)
                .build();
    }

    public boolean isExpired() {
        return expireDate.isBefore(LocalDateTime.now());
    }
}
