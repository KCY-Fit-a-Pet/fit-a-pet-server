package kr.co.fitapet.infra.client.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import kr.co.fitapet.infra.config.NcpConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NcpObjectStorageService {
    private final AmazonS3 amazonS3;
    private final NcpConfig config;

    public void deleteObjects(List<String> paths) {
        paths.replaceAll(path -> path.replace("https://" + config.getS3().bucket() + ".kr.object.ncloudstorage.com/", ""));
        List<DeleteObjectsRequest.KeyVersion> keys = paths.stream().map(DeleteObjectsRequest.KeyVersion::new).toList();

        try {
            amazonS3.deleteObjects(new DeleteObjectsRequest(config.getS3().bucket()).withKeys(keys));
            log.info("NCP Object Storage deleteObjects success : {}", paths);
        } catch (Exception e) {
            log.error("NCP Object Storage deleteObject error: {}", e.getMessage());
        }
    }
}
