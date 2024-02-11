package kr.co.fitapet.domain.domains.memo.domain;

import jakarta.persistence.*;
import kr.co.fitapet.domain.common.model.DateAuditable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
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

    public static MemoImage of(String imgUrl, Memo memo) {
        MemoImage memoImage = new MemoImage(imgUrl);
        memoImage.updateMemo(memo);
        return memoImage;
    }

    public void updateMemo(Memo memo) {
        if (this.memo != null) {
            this.memo.getMemoImages().remove(this);
        }

        this.memo = memo;

        if (memo != null)
            memo.getMemoImages().add(this);
    }
}
