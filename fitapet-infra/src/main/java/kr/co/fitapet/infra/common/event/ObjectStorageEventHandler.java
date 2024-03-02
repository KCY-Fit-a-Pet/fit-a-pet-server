package kr.co.fitapet.infra.common.event;

import kr.co.fitapet.infra.client.s3.NcpObjectStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class ObjectStorageEventHandler {
    private final NcpObjectStorageService objectStorageService;

    /**
     * Object Storage에 저장된 이미지를 삭제합니다.
     * <br/>
     * {@link ObjectStorageImageDeleteEvent}를 받아서 Object Storage에 저장된 이미지를 삭제합니다.
     * <br/>
     * 해당 이벤트는 트랜잭션 내에서 실행되지 않습니다.
     * @param event {@link ObjectStorageImageDeleteEvent}
     */
    @TransactionalEventListener
    public void handleObjectStorageImageDeleteEvent(ObjectStorageImageDeleteEvent event) {
        log.info("handleObjectStorageImageDeleteEvent: {}", event);
        objectStorageService.deleteObjects(event.imageUrls());
        log.info("Successfully deleted images from Object Storage");
    }
}
