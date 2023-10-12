package com.kcy.fitapet.domain.member.domain;

import com.kcy.fitapet.domain.model.Auditable;
import com.kcy.fitapet.domain.pet.domain.Pet;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "MANAGER")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Manager extends Auditable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ColumnDefault("false")
    private boolean hidden;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member manager;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    private Pet pet;

    public void hide() {
        this.hidden = true;
    }

    public void show() {
        this.hidden = false;
    }
}
