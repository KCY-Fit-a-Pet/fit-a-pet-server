package com.kcy.fitapet.domain.memo.domain;

import com.kcy.fitapet.domain.member.domain.Member;
import com.kcy.fitapet.domain.model.Auditable;
import com.kcy.fitapet.domain.pet.domain.Pet;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "MEMO")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Memo extends Auditable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    private Pet pet;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", updatable = false)
    private Member author;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_editor_id")
    private Member lastEditor;
    @OneToMany(mappedBy = "memo", cascade = CascadeType.ALL)
    private List<MemoImage> memoImages;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Builder
    private Memo(String content, Pet pet, Member author, Member lastEditor, List<MemoImage> memoImages, Category category) {
        this.content = content;
        this.pet = pet;
        this.author = author;
        this.lastEditor = lastEditor;
        this.memoImages = memoImages;
        this.category = category;
    }

    public static Memo of(String content, Pet pet, Member author, Member lastEditor, List<MemoImage> memoImages, Category category) {
        return Memo.builder()
                .content(content)
                .pet(pet)
                .author(author)
                .lastEditor(lastEditor)
                .memoImages(memoImages)
                .category(category)
                .build();
    }
}
