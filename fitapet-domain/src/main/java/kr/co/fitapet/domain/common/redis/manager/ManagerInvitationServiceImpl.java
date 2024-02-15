package kr.co.fitapet.domain.common.redis.manager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class ManagerInvitationServiceImpl implements ManagerInvitationService {
    private final ManagerInvitationRepository managerInvitationRepository;

    @Override
    public void save(Long to, Long petId, Long from) {
        final long timeToLive = Duration.ofDays(1).toSeconds();

        log.info("manager invitation ttl : {}", timeToLive);

        ManagerInvitation managerInvitation = ManagerInvitation.of(to, petId, from, timeToLive);
        managerInvitationRepository.save(managerInvitation);
        log.info("manager invitation registered. about User : {}", managerInvitation.getTo());
    }

    @Override
    public boolean exists(Long to, Long petId) {
        return managerInvitationRepository.existsById(to + "@" + petId);
    }

    @Override
    public void delete(Long to, Long petId) {
        managerInvitationRepository.deleteById(to + "@" + petId);
    }
}
