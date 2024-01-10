package com.kcy.fitapet.domain.care.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kcy.fitapet.domain.member.domain.Member;
import com.kcy.fitapet.domain.model.DateAuditable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CARE_CATEGORY")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
public class CareCategory extends DateAuditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String categoryName;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Care> cares = new ArrayList<>();

    @CreatedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false, updatable = false)
    @JsonIgnore
    private Member author;

    private CareCategory(String categoryName) {
        this.categoryName = categoryName;
    }

    public static CareCategory of(String categoryName) {
        return new CareCategory(categoryName);
    }
}
