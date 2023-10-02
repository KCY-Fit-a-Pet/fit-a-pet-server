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
@Table(name = "CATEGORY")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"categoryName"})
public class Category extends Auditable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "category_name")
    private String categoryName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Category> children = new ArrayList<>();

    @Builder
    private Category(String categoryName, Category parent) {
        this.categoryName = categoryName;
        this.parent = parent;
    }

    public static Category of(String categoryName, Category parent) {
        return Category.builder()
                .categoryName(categoryName)
                .parent(parent)
                .build();
    }
}
