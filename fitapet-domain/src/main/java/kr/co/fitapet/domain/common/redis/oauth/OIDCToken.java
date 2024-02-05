package kr.co.fitapet.domain.common.redis.oauth;

import com.kcy.fitapet.domain.oauth.type.ProviderType;
import lombok.Builder;
import org.springframework.data.annotation.Id;
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
    public OIDCToken(String token, String providerWithId, long ttl) {
        this.token = token;
        this.providerWithId = providerWithId;
        this.ttl = ttl;
    }

    public static OIDCToken of(String token, ProviderType provider, String id, long ttl) {
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

    public String getId() {
        return providerWithId.split("@")[1];
    }
}
