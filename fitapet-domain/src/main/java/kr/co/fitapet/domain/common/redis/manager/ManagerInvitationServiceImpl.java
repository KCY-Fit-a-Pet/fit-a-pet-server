package kr.co.fitapet.domain.common.redis.manager;

import kr.co.fitapet.domain.common.redis.exception.RedisErrorCode;
import kr.co.fitapet.domain.common.redis.exception.RedisErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ManagerInvitationServiceImpl implements ManagerInvitationService {
    private final ManagerInvitationRepository managerInvitationRepository;

    @Override
    public void save(Long invitedId, Long petId) {
        if (managerInvitationRepository.exists(petId.toString(), invitedId.toString())) {
            log.warn("already invited. about User : {}", invitedId);
            throw new RedisErrorException(RedisErrorCode.ALREADY_EXISTS_VALUE);
        }

        LocalDateTime timeToLive = LocalDateTime.now().plusDays(1);
        log.info("manager invitation ttl : {}", timeToLive);

        managerInvitationRepository.save(petId.toString(), invitedId.toString(), timeToLive);
        log.info("manager invitation registered. about User : {}", invitedId);
    }

    @Override
    public InvitationDto findInvitationInfo(Long invitedId, Long petId) {
        return InvitationDto.of(petId, invitedId, managerInvitationRepository.getTtl(petId.toString(), invitedId.toString()));
    }

    @Override
    public List<InvitationDto> findAll(Long petId) {
        Map<Object, Object> all = managerInvitationRepository.findAll(petId.toString());
        return all.entrySet().stream()
                .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), LocalDateTime.parse((CharSequence) entry.getValue())))
                .map(entry -> InvitationDto.of(petId, Long.parseLong((String) entry.getKey()), entry.getValue()))
                .toList();
    }

    @Override
    public boolean expired(Long invitedId, Long petId) {
        if (!managerInvitationRepository.exists(petId.toString(), invitedId.toString())) {
            log.warn("not found invitation. about User : {}", invitedId);
            throw new RedisErrorException(RedisErrorCode.NOT_FOUND_KEY);
        }
        LocalDateTime ttl = managerInvitationRepository.getTtl(invitedId.toString(), petId.toString());
        return ttl.isBefore(LocalDateTime.now());
    }

    @Override
    public void delete(Long invitedId, Long petId) {
        managerInvitationRepository.delete(invitedId.toString(), petId.toString());
    }
}
