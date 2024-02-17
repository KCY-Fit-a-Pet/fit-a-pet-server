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
        return all.entrySet().stream()
                .map(entry -> InvitationDto.of(petId, entry.getKey(), entry.getValue()))
                .toList();
    }

    @Override
    public boolean expired(Long invitedId, Long petId) {
        isValid(invitedId, petId);
        LocalDateTime ttl = managerInvitationRepository.getTtl(petId.toString(), invitedId);
        log.info("manager invitation ttl : {}", ttl);

        return ttl.isBefore(LocalDateTime.now());
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
