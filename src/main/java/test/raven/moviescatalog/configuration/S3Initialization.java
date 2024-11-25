package test.raven.moviescatalog.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.BucketAlreadyOwnedByYouException;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.CreateBucketResponse;

@Component
@RequiredArgsConstructor
public class S3Initialization {

    private final S3Client s3Client;

    @EventListener
    public void onApplicationReady(ApplicationReadyEvent event) {
        try {
            CreateBucketRequest createBucketRequest = CreateBucketRequest.builder()
                    .bucket("images")
                    .acl("public-read")
                    .build();
            CreateBucketResponse bucket = s3Client.createBucket(createBucketRequest);
        } catch (BucketAlreadyOwnedByYouException ignored) {}
    }
}
