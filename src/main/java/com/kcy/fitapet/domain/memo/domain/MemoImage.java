package com.kcy.fitapet.domain.memo.domain;

import com.kcy.fitapet.domain.model.DateAuditable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "MEMO_IMAGE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemoImage extends DateAuditable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "img_url")
    private String imgUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memo_id")
    private Memo memo;

    @Builder
    private MemoImage(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public static MemoImage of(String imgUrl) {
        return MemoImage.builder()
                .imgUrl(imgUrl).build();
    }
}
