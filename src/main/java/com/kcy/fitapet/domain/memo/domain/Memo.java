package com.kcy.fitapet.domain.memo.domain;

import com.kcy.fitapet.domain.member.domain.Member;
import com.kcy.fitapet.domain.model.DateAuditable;
import com.kcy.fitapet.domain.pet.domain.Pet;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "MEMO")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Memo extends DateAuditable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
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
    private MemoCategory memoCategory;

    @Builder
    private Memo(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public static Memo of(String title, String content) {
        return Memo.builder()
                .title(title)
                .content(content)
                .build();
    }
}
