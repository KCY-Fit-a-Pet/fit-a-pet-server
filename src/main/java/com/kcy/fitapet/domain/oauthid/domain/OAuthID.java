package com.kcy.fitapet.domain.oauthid.domain;

import com.kcy.fitapet.domain.member.domain.Member;
import com.kcy.fitapet.domain.model.Auditable;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "OAUTH_ID")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "provider"})
public class OAuthID extends Auditable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "access_token")
    private String accessToken;
    private String provider;
    @Column(name = "expired_time")
    private Long expired_time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    private OAuthID(String accessToken, String provider, Long expired_time) {
        this.accessToken = accessToken;
        this.provider = provider;
        this.expired_time = expired_time;
    }

    public static OAuthID of(String accessToken, String provider, Long expired_time) {
        return OAuthID.builder()
                .accessToken(accessToken)
                .provider(provider)
                .expired_time(expired_time)
                .build();
    }
}
