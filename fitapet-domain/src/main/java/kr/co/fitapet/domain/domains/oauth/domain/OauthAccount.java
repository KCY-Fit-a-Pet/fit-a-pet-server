package kr.co.fitapet.domain.domains.oauth.domain;

import jakarta.persistence.*;
import kr.co.fitapet.domain.common.model.DateAuditable;
import kr.co.fitapet.domain.domains.member.domain.Member;
import kr.co.fitapet.domain.domains.oauth.type.ProviderType;
import kr.co.fitapet.domain.common.converter.ProviderTypeConverter;
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
    private String oauthId;
    @Convert(converter = ProviderTypeConverter.class)
    private ProviderType provider;
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public OauthAccount(Long id, String oauthId, ProviderType provider, String email) {
        this.id = id;
        this.oauthId = oauthId;
        this.provider = provider;
        this.email = email;
    }

    public static OauthAccount of(String oauthId, ProviderType provider, String email) {
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
