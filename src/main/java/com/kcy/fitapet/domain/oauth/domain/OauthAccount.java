package com.kcy.fitapet.domain.oauth.domain;

import com.kcy.fitapet.domain.member.domain.Member;
import com.kcy.fitapet.domain.model.Auditable;
import com.kcy.fitapet.domain.oauth.type.ProviderType;
import com.kcy.fitapet.domain.oauth.type.converter.ProviderTypeConverter;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "OAUTH")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "provider"})
public class OauthAccount extends Auditable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "oauth_id")
    private Long OauthId;
    @Convert(converter = ProviderTypeConverter.class)
    private ProviderType provider;
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public OauthAccount(Long id, Long OauthId, ProviderType provider, String email, Member member) {
        this.id = id;
        this.OauthId = OauthId;
        this.provider = provider;
        this.email = email;
        this.member = member;
    }

    public static OauthAccount of(Long OauthId, ProviderType provider, String email, Member member) {
        return OauthAccount.builder()
                .OauthId(OauthId)
                .provider(provider)
                .email(email)
                .member(member)
                .build();
    }
}
