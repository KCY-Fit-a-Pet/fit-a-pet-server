package kr.co.fitapet.domain.common.redis.manager;

import kr.co.fitapet.domain.common.redis.exception.RedisErrorCode;
import kr.co.fitapet.domain.common.redis.exception.RedisErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ManagerInvitationServiceImpl implements ManagerInvitationService {
    private final ManagerInvitationRepository managerInvitationRepository;

    @Override
    public void save(Long invitedId, Long petId) {
        if (managerInvitationRepository.exists(petId.toString(), invitedId)) {
            log.warn("already invited. about User : {}", invitedId);
            throw new RedisErrorException(RedisErrorCode.ALREADY_EXISTS_VALUE);
        }

        LocalDateTime timeToLive = LocalDateTime.now().plusDays(1);
        log.info("manager invitation ttl : {}", timeToLive);

        managerInvitationRepository.save(petId.toString(), invitedId, timeToLive);
        log.info("manager invitation registered. about User : {}", invitedId);
    }

    @Override
    public List<InvitationDto> findAll(Long petId) {
        Map<Long, LocalDateTime> all = managerInvitationRepository.findAll(petId.toString());

        all.forEach((k, v) -> {
            boolean expired = v.isBefore(LocalDateTime.now());

            if (expired) {
                log.warn("manager invitation expired. about User : {}", k);
                delete(k, petId);
            }
        });

        return all.entrySet().stream()
                .filter(entry -> !entry.getValue().isBefore(LocalDateTime.now()))
                .map(entry -> InvitationDto.of(petId, entry.getKey(), entry.getValue()))
                .toList();
    }

    @Override
    public boolean expired(Long invitedId, Long petId) {
        isValid(invitedId, petId);
        LocalDateTime ttl = managerInvitationRepository.getTtl(petId.toString(), invitedId);
        log.info("manager invitation ttl : {}", ttl);

        boolean expired = ttl.isBefore(LocalDateTime.now());

        if (expired) {
            log.warn("manager invitation expired. about User : {}", invitedId);
            delete(invitedId, petId);
            return true;
        }
        return false;
    }

    @Override
    public void delete(Long invitedId, Long petId) {
        isValid(invitedId, petId);
        managerInvitationRepository.delete(petId.toString(), invitedId);
    }

    private void isValid(Long invitedId, Long petId) {
        if (!managerInvitationRepository.exists(petId.toString(), invitedId)) {
            log.warn("not found invitation. about User : {}", invitedId);
            throw new RedisErrorException(RedisErrorCode.NOT_FOUND_KEY);
        }
    }
}
