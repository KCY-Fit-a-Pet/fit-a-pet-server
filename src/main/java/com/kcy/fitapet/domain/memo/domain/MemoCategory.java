package com.kcy.fitapet.domain.memo.domain;

import com.kcy.fitapet.domain.model.DateAuditable;
import com.kcy.fitapet.domain.pet.domain.Pet;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "MEMO_CATEGORY")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"categoryName"})
public class MemoCategory extends DateAuditable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "category_name")
    private String categoryName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    private Pet pet;
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

    public void updatePet(Pet pet) {
        if (this.pet != null)
            this.pet.getMemoCategories().remove(this);

        this.pet = pet;
        pet.getMemoCategories().add(this);
    }

    public void updateParent(MemoCategory parent) {
        if (this.parent != null)
            this.parent.getChildren().remove(this);

        this.parent = parent;
        parent.getChildren().add(this);
    }
}
