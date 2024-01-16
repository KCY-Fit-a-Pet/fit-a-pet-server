package com.kcy.fitapet.domain.log.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Transient;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@EqualsAndHashCode
public class CareLogId implements Serializable {
    @Transient
    private static final long serialVersionUID = 1L;

    @Column(name = "id")
    private Long id;
    @Column(name = "log_date")
    private LocalDateTime logDate;
}
