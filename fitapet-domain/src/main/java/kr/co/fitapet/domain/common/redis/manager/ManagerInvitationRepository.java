package kr.co.fitapet.domain.common.redis.manager;

import java.time.LocalDateTime;
import java.util.Map;

public interface ManagerInvitationRepository {
    void save(String petId, Long invitedId, LocalDateTime ttl);

    Boolean exists(String petId, Long invitedId);
    LocalDateTime getTtl(String petId, Long invitedId);
    Map<Long, LocalDateTime> findAll(String petId);
    void delete(String petId, Long invitedId);
}
