package kr.co.fitapet.domain.common.redis.manager;

import java.time.LocalDateTime;
import java.util.Map;

public interface ManagerInvitationRepository {
    void save(String petId, String invitedId, LocalDateTime ttl);

    Boolean exists(String petId, String invitedId);
    LocalDateTime getTtl(String petId, String invitedId);
    Map<Object, Object> findAll(String petId);
    void delete(String petId, String invitedId);
}
