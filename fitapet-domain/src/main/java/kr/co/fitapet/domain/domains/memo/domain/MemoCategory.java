package kr.co.fitapet.domain.domains.memo.domain;

import jakarta.persistence.*;
import kr.co.fitapet.domain.common.model.DateAuditable;
import kr.co.fitapet.domain.domains.pet.domain.Pet;
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

    @OneToMany(mappedBy = "memoCategory", cascade = CascadeType.ALL)
    private List<Memo> memos = new ArrayList<>();

    @Builder
    private MemoCategory(String categoryName) {
        this.categoryName = categoryName;
    }

    public static MemoCategory ofChildrenInstance(String categoryName, MemoCategory parent, Pet pet) {
        MemoCategory category = MemoCategory.builder().categoryName(categoryName).build();
        category.updatePet(pet);
        category.updateParent(parent);
        return category;
    }

    public static MemoCategory ofRootInstance(String categoryName, Pet pet) {
        MemoCategory category = MemoCategory.builder().categoryName(categoryName).build();
        category.updatePet(pet);
        return category;
    }

    private void updatePet(Pet pet) {
        if (this.pet != null)
            this.pet.getMemoCategories().remove(this);

        this.pet = pet;
        pet.getMemoCategories().add(this);
    }

    private void updateParent(MemoCategory parent) {
        if (this.parent != null)
            this.parent.getChildren().remove(this);

        this.parent = parent;
        parent.getChildren().add(this);
    }

    public boolean isRootCategory() {
        return this.parent == null;
    }
}
