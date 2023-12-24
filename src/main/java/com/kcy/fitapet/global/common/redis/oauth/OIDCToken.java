package com.kcy.fitapet.global.common.redis.oauth;

import com.kcy.fitapet.domain.oauth.type.ProviderType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash("oidcToken")
public class OIDCToken {
    @Id
    private final String token;
    private final String providerWithId;
    @TimeToLive
    private final long ttl;

    @Builder
    public OIDCToken(String token, ProviderType provider, Long id, long ttl) {
        this.token = token;
        this.providerWithId = provider.name() + "@" + id;
        this.ttl = ttl;
    }

    public static OIDCToken of(String token, ProviderType provider, Long id, long ttl) {
        return OIDCToken.builder()
                .token(token)
                .provider(provider)
                .id(id)
                .ttl(ttl)
                .build();
    }

    public String getToken() {
        return token;
    }

    public String getProvider() {
        return providerWithId.split("@")[0];
    }

    public Long getId() {
        return Long.parseLong(providerWithId.split("@")[1]);
    }
}
