package kr.co.fitapet.domain.common.redis.manager;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Map;

@Repository
public class ManagerInvitationRepositoryImpl implements ManagerInvitationRepository {

    private final HashOperations<String, Object, Object> ops;

    public ManagerInvitationRepositoryImpl(RedisTemplate<String, Object> redisTemplate) {
        this.ops = redisTemplate.opsForHash();
    }

    @Override
    public void save(String petId, String invitedId, LocalDateTime ttl) {
        ops.put(petId, invitedId, ttl.toString());
    }

    @Override
    public Boolean exists(String petId, String invitedId) {
        return ops.hasKey(petId, invitedId);
    }

    @Override
    public LocalDateTime getTtl(String petId, String invitedId) {
        return (LocalDateTime) ops.get(petId, invitedId);
    }

    @Override
    public Map<Object, Object> findAll(String petId) {
        return ops.entries(petId);
    }

    @Override
    public void delete(String petId, String invitedId) {
        ops.delete(petId, invitedId);
    }
}