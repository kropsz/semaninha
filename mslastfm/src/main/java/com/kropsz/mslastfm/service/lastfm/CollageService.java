package com.kropsz.mslastfm.service.lastfm;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import org.springframework.stereotype.Service;

import com.kropsz.mslastfm.data.model.Collage;
import com.kropsz.mslastfm.data.model.UserData;
import com.kropsz.mslastfm.dto.Request;
import com.kropsz.mslastfm.feign.LastfmClient;
import com.kropsz.mslastfm.service.s3.S3Service;
import com.kropsz.mslastfm.service.user.UserService;
import com.kropsz.mslastfm.util.CollageConstructor;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class CollageService {

    private final LastfmClient lastfmClient;
    private final CollageConstructor collageConstructor;
    private final S3Service s3Service;
    private final UserService userService;

    public Collage createCollage(Request request) throws IOException {
        var user = userService.getUserData(request.user());

        var response = lastfmClient.getTopAlbums(request);
        var image =  collageConstructor.drawImagesInGrid(response, request.limit());
        return saveCollage(image, user);
    }

    public Collage saveCollage(BufferedImage image, UserData user) throws IOException {
        String fileName = "collage_" + user.getUser() + "_" + user.getCollages().size() + ".png";
        s3Service.uploadImage(image, fileName);
        return userService.addCollageToUser(user, getCollageUrl(fileName));

    }

    public URL getCollageUrl(String fileName){
        return s3Service.getPublicUrl(fileName);
    }


}