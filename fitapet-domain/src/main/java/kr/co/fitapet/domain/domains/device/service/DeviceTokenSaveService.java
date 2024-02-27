package kr.co.fitapet.domain.domains.device.service;

import kr.co.fitapet.common.annotation.DomainService;
import kr.co.fitapet.domain.domains.device.domain.DeviceToken;
import kr.co.fitapet.domain.domains.device.repository.DeviceTokenRepository;
import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class DeviceTokenSaveService {
    private final DeviceTokenRepository deviceTokenRepository;

    public void save(DeviceToken deviceToken) {
        deviceTokenRepository.save(deviceToken);
    }
}
