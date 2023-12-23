package com.kcy.fitapet.domain.oauth.domain;

import com.kcy.fitapet.domain.member.domain.Member;
import com.kcy.fitapet.domain.model.Auditable;
import com.kcy.fitapet.domain.oauth.type.ProviderType;
import com.kcy.fitapet.domain.oauth.type.converter.ProviderTypeConverter;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "OAUTH")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "provider"})
public class OAuthAccount extends Auditable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "oauth_id")
    private Long OAuthID;
    @Convert(converter = ProviderTypeConverter.class)
    private ProviderType provider;
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public OAuthAccount(Long id, Long OAuthID, ProviderType provider, String email, Member member) {
        this.id = id;
        this.OAuthID = OAuthID;
        this.provider = provider;
        this.email = email;
        this.member = member;
    }

    public static OAuthAccount of(Long OAuthID, ProviderType provider, String email, Member member) {
        return OAuthAccount.builder()
                .OAuthID(OAuthID)
                .provider(provider)
                .email(email)
                .member(member)
                .build();
    }
}
