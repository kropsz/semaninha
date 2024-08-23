package com.kropsz.mslastfm.service.s3;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

import com.kropsz.mslastfm.exception.ValueRequiredException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.core.sync.RequestBody;

@Service
public class S3Service {

    private final S3Client s3Client;
    private final String bucketName;

    public S3Service(S3Client s3Client, @Value("${aws.s3.bucketName}") String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    public void uploadImage(BufferedImage image, String keyName) throws IOException {
        if (image == null || keyName == null || keyName.isEmpty()) {
            throw new IllegalArgumentException("Image or keyName cannot be null or empty");
        }

        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", os);
            byte[] imageBytes = os.toByteArray();

            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes)) {
                PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(keyName)
                        .build();

                s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, imageBytes.length));
            }
        }
    }

    public URL getPublicUrl(String key) {
        if (key == null || key.isEmpty()) {
            throw new ValueRequiredException("Key cannot be null or empty");
        }

        GetUrlRequest request = GetUrlRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        return s3Client.utilities().getUrl(request);
    }
}