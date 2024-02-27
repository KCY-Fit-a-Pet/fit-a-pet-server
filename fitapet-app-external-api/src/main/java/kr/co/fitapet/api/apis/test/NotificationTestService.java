package kr.co.fitapet.api.apis.test;

import kr.co.fitapet.domain.domains.device.domain.DeviceToken;
import kr.co.fitapet.domain.domains.device.service.DeviceTokenSearchService;
import kr.co.fitapet.infra.client.fcm.NotificationDataKey;
import kr.co.fitapet.infra.client.fcm.NotificationService;
import kr.co.fitapet.infra.client.fcm.request.NotificationRequest;
import kr.co.fitapet.infra.client.fcm.request.NotificationSingleRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationTestService {
    private final DeviceTokenSearchService deviceTokenSearchService;
    private final NotificationService notificationService;

    public void push(Long userId) {
        log.info("push to {}", userId);

        List<DeviceToken> deviceTokens = deviceTokenSearchService.findDeviceTokensByMemberId(userId);
        log.info("deviceTokens: {}", deviceTokens);

        log.info("send push message");
        deviceTokens.forEach(deviceToken -> {
            NotificationSingleRequest request = NotificationSingleRequest.builder()
                    .token(deviceToken.getDeviceToken())
                    .title("푸시 테스트")
                    .content("푸시 테스트 메시지")
                    .build();

            notificationService.sendMessage(request);
        });

        log.info("send push message with image");
        deviceTokens.forEach(deviceToken -> {
            NotificationSingleRequest request = NotificationSingleRequest.builder()
                    .title("이미지 테스트")
                    .content("푸시 이미지 테스트 메시지")
                    .imageUrl("https://pkcy.kr.object.ncloudstorage.com/profile/0bb25dde9020.jpeg")
                    .build();

            notificationService.sendMessage(request);
        });

        log.info("send push message with data");
        deviceTokens.forEach(deviceToken -> {
            NotificationSingleRequest request = NotificationSingleRequest.builder()
                    .title("데이터 테스트")
                    .content("푸시 데이터 테스트 메시지")
                    .data(
                            Map.of(
                                    NotificationDataKey.NOTICE_TYPE.getField(), "TEST_TYPE",
                                    NotificationDataKey.TO_ID.getField(), userId.toString()
                            )
                    )
                    .build();

            notificationService.sendMessage(request);
        });
    }
}
