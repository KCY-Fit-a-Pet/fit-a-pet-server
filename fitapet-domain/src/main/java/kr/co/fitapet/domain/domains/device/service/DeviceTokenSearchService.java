package kr.co.fitapet.domain.domains.device.service;

import kr.co.fitapet.common.annotation.DomainService;
import kr.co.fitapet.domain.domains.device.domain.DeviceToken;
import kr.co.fitapet.domain.domains.device.repository.DeviceTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@DomainService
@RequiredArgsConstructor
public class DeviceTokenSearchService {
    private final DeviceTokenRepository deviceTokenRepository;

    @Transactional(readOnly = true)
    public List<DeviceToken> findDeviceTokensByMemberId(Long userId) {
        return deviceTokenRepository.findAllByMember_Id(userId);
    }

    @Transactional(readOnly = true)
    public List<DeviceToken> findAll() {
        return deviceTokenRepository.findAll();
    }

    @Transactional(readOnly = true)
    public boolean isExistDeviceToken(String deviceToken) {
        return deviceTokenRepository.existsByDeviceToken(deviceToken);
    }
}
