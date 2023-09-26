package com.kcy.fitapet.domain.memo.domain;

import com.kcy.fitapet.domain.model.Auditable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "MEMO_IMAGE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemoImage extends Auditable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "img_url")
    private String imgUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memo_id")
    private Memo memo;

    @Builder
    private MemoImage(String imgUrl, Memo memo) {
        this.imgUrl = imgUrl;
        this.memo = memo;
    }

    public static MemoImage of(String imgUrl, Memo memo) {
        return MemoImage.builder()
                .imgUrl(imgUrl).memo(memo).build();
    }
}
