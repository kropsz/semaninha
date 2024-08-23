package com.kropsz.mslastfm.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kropsz.mslastfm.exception.ValueRequiredException;
import com.kropsz.mslastfm.service.s3.S3Service;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Utilities;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@ExtendWith(MockitoExtension.class)
class S3ServiceTest {
    @Mock
    private S3Client s3Client;

    @Mock
    private S3Utilities s3Utilities;

    @InjectMocks
    private S3Service s3Service;

    @BeforeEach
    void setUp() {
        s3Service = new S3Service(s3Client, "test-bucket");
    }

    @Test
    @DisplayName("Should upload image to S3")
    void testUploadImage() throws IOException {
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        String keyName = "test-key";

        s3Service.uploadImage(image, keyName);

        verify(s3Client, times(1)).putObject(any(PutObjectRequest.class),
                any(RequestBody.class));
    }

    @Test
    @DisplayName("Should throw exception when image is null")
    void testUploadImageThrowsExceptionWhenImageIsNull() {
        String keyName = "test-key";
        assertThrows(IllegalArgumentException.class, () -> s3Service.uploadImage(null, keyName));
    }

    @Test
    @DisplayName("Should throw exception when key name is null")
    void testUploadImageThrowsExceptionWhenKeyNameIsNull() {
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        assertThrows(IllegalArgumentException.class, () -> s3Service.uploadImage(image, null));
    }

    @Test
    @DisplayName("Should throws exception when key name is empty")
    void testUploadImageThrowsExceptionWhenKeyNameIsEmpty() {
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        assertThrows(IllegalArgumentException.class, () -> s3Service.uploadImage(image, ""));
    }

    @Test
    @DisplayName("Should return public URL")
    void testGetPublicUrl() throws Exception {
        when(s3Client.utilities()).thenReturn(s3Utilities);
        String key = "test-key";
        @SuppressWarnings("deprecation")
        URL expectedUrl = new URL("https://test-bucket.s3.amazonaws.com/test-key");

        when(s3Utilities.getUrl(any(GetUrlRequest.class))).thenReturn(expectedUrl);

        URL actualUrl = s3Service.getPublicUrl(key);

        assertEquals(expectedUrl, actualUrl);
        verify(s3Utilities, times(1)).getUrl(any(GetUrlRequest.class));
    }

    @Test
    @DisplayName("Should throw exception when key is null")
    void testGetPublicUrlThrowsExceptionWhenKeyIsNull() {
        assertThrows(ValueRequiredException.class, () -> s3Service.getPublicUrl(null));
    }

    @Test
    @DisplayName("Should throw exception when key is empty")
    void testGetPublicUrlThrowsExceptionWhenKeyIsEmpty() {
        assertThrows(ValueRequiredException.class, () -> s3Service.getPublicUrl(""));
    }
}