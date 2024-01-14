package com.kcy.fitapet.global.common.redis.oauth;

import com.kcy.fitapet.domain.oauth.type.ProviderType;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.math.BigInteger;

@RedisHash("oidcToken")
public class OIDCToken {
    @Id
    private final String token;
    private final String providerWithId;
    @TimeToLive
    private final long ttl;

    @Builder
    public OIDCToken(String token, String providerWithId, long ttl) {
        this.token = token;
        this.providerWithId = providerWithId;
        this.ttl = ttl;
    }

    public static OIDCToken of(String token, ProviderType provider, BigInteger id, long ttl) {
        return OIDCToken.builder()
                .token(token)
                .providerWithId(provider.name() + "@" + id)
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
