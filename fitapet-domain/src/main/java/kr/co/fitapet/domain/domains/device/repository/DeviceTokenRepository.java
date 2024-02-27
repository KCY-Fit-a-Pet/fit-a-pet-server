package kr.co.fitapet.domain.domains.device.repository;

import kr.co.fitapet.domain.domains.device.domain.DeviceToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceTokenRepository extends JpaRepository<DeviceToken, Long> {
    List<DeviceToken> findAllByMember_Id(Long userId);
    boolean existsByDeviceToken(String deviceToken);
}
