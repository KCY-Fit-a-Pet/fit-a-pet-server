package com.kcy.fitapet.domain.oauth.domain;

import com.kcy.fitapet.domain.member.domain.Member;
import com.kcy.fitapet.domain.model.DateAuditable;
import com.kcy.fitapet.domain.oauth.type.ProviderType;
import com.kcy.fitapet.domain.oauth.type.converter.ProviderTypeConverter;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "OAUTH")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "provider"})
public class OauthAccount extends DateAuditable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "oauth_id")
    private Long oauthId;
    @Convert(converter = ProviderTypeConverter.class)
    private ProviderType provider;
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public OauthAccount(Long id, Long oauthId, ProviderType provider, String email) {
        this.id = id;
        this.oauthId = oauthId;
        this.provider = provider;
        this.email = email;
    }

    public static OauthAccount of(Long oauthId, ProviderType provider, String email) {
        return OauthAccount.builder()
                .oauthId(oauthId)
                .provider(provider)
                .email(email)
                .build();
    }

    public void updateMember(Member member) {
        if (this.member != null) {
            this.member.getOauthAccounts().remove(this);
        }

        this.member = member;
        member.getOauthAccounts().add(this);
    }
}
