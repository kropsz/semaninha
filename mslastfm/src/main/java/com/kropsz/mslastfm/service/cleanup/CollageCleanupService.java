package com.kropsz.mslastfm.service.cleanup;

import java.time.LocalDate;
import java.util.Iterator;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.kropsz.mslastfm.data.model.Collage;
import com.kropsz.mslastfm.data.model.UserData;
import com.kropsz.mslastfm.data.repository.UserDataRepository;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

@Service
@RequiredArgsConstructor
public class CollageCleanupService {

    private final UserDataRepository userDataRepository;
    private final S3Client s3Client;

    private static final String BUCKET_NAME = "collage-images-bucket";

    @Scheduled(fixedRate = 3600000) // Executa a cada 1 hora
    public void cleanUpOldCollages() {
        Iterable<UserData> users = userDataRepository.findAll();
        for (UserData user : users) {
            Iterator<Collage> iterator = user.getCollages().iterator();
            while (iterator.hasNext()) {
                Collage collage = iterator.next();
                if (collage.getDate().isBefore(LocalDate.now().minusDays(45))) {
                    String key = collage.getImageUrl().getPath().substring(1);
                    s3Client.deleteObject(DeleteObjectRequest.builder()
                            .bucket(BUCKET_NAME)
                            .key(key)
                            .build());
                    
                    iterator.remove();
                }
            }
            userDataRepository.save(user); 
        }
    }
}