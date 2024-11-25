package test.raven.moviescatalog.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageHandler {

    private final S3Client s3Client;

    public String handleImage(MultipartFile file) throws IOException {

        String id = UUID.randomUUID().toString();

        String originalFilename = file.getOriginalFilename();
        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));

        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket("images")
                        .key(id + substring)
                        .contentType(file.getContentType())
                        .build(),
                software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes()));

        return id + substring;
    }
}
