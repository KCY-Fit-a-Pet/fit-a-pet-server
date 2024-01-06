package com.kcy.fitapet.domain.memo.domain;

import com.kcy.fitapet.domain.model.Auditable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "MEMO_CATEGORY")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"categoryName"})
public class MemoCategory extends Auditable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "category_name")
    private String categoryName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private MemoCategory parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<MemoCategory> children = new ArrayList<>();

    @Builder
    private MemoCategory(String categoryName, MemoCategory parent) {
        this.categoryName = categoryName;
        this.parent = parent;
    }

    public static MemoCategory of(String categoryName, MemoCategory parent) {
        return MemoCategory.builder()
                .categoryName(categoryName)
                .parent(parent)
                .build();
    }
}
