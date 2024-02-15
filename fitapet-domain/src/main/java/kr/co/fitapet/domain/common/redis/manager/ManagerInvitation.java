package kr.co.fitapet.domain.common.redis.manager;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash("managerInvitation")
public class ManagerInvitation {
    @Id
    private final String toWithPetId;
    private final Long from;
    @TimeToLive
    private final long ttl;

    @Builder
    private ManagerInvitation(String toWithPetId, Long from, long ttl) {
        this.toWithPetId = toWithPetId;
        this.from = from;
        this.ttl = ttl;
    }

    public static ManagerInvitation of(Long to, Long petId, Long from, long ttl) {
        return ManagerInvitation.builder()
                .toWithPetId(to + "@" + petId)
                .from(from)
                .ttl(ttl)
                .build();
    }

    public Long getFrom() {
        return from;
    }

    public Long getTo() {
        return Long.parseLong(toWithPetId.split("@")[0]);
    }

    public Long getPetId() {
        return Long.parseLong(toWithPetId.split("@")[1]);
    }
}
