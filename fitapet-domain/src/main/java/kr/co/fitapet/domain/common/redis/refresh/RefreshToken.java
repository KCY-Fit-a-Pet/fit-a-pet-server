package kr.co.fitapet.domain.common.redis.refresh;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash("refreshToken")
@Getter
@ToString(of = {"userId", "token"})
public class RefreshToken {
    @Id
    private final Long userId;
    private String token;
    @TimeToLive
    private final long ttl;

    @Builder
    private RefreshToken(String token, Long userId, long ttl) {
        this.token = token;
        this.userId = userId;
        this.ttl = ttl;
    }

    protected void rotation(String token) {
        this.token = token;
    }
}