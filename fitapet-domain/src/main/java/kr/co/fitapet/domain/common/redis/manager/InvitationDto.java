package kr.co.fitapet.domain.common.redis.manager;

import java.time.LocalDateTime;
import java.util.Objects;

public record InvitationDto(
        Long petId,
        Long inviteId,
        LocalDateTime ttl,
        Boolean expired
) {
    public InvitationDto {
        Objects.requireNonNull(petId, "petId must be provided");
        Objects.requireNonNull(inviteId, "inviteId must be provided");
        Objects.requireNonNull(ttl, "ttl must be provided");
    }

    public static InvitationDto of(Long petId, Long inviteId, LocalDateTime ttl) {
        return new InvitationDto(petId, inviteId, ttl.minusDays(1), ttl.isBefore(LocalDateTime.now()));
    }
}
