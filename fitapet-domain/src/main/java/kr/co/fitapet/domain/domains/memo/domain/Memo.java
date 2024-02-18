package kr.co.fitapet.domain.domains.memo.domain;

import jakarta.persistence.*;
import kr.co.fitapet.domain.common.model.AuthorAuditable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "MEMO")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Memo extends AuthorAuditable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;

    @OneToMany(mappedBy = "memo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemoImage> memoImages = new ArrayList<>();

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

    public void updateMemoCategory(MemoCategory memoCategory) {
        if (this.memoCategory != null) {
            this.memoCategory.getMemos().remove(this);
        }

        this.memoCategory = memoCategory;
        memoCategory.getMemos().add(this);
    }

    public void updateMemo(Memo memo) {
        this.title = memo.title;
        this.content = memo.content;
    }
}
