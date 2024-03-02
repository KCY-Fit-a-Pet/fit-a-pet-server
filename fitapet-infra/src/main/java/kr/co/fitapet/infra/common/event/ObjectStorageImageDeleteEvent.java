package kr.co.fitapet.infra.common.event;

import java.util.List;

/**
 * Object Storage Image Delete Event를 위한 Object
 */
public record ObjectStorageImageDeleteEvent(
        List<String> imageUrls
) {
    public static ObjectStorageImageDeleteEvent valueOf(List<String> imageUrls) {
        return new ObjectStorageImageDeleteEvent(imageUrls);
    }
}
