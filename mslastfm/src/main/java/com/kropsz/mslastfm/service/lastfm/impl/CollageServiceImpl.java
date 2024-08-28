package com.kropsz.mslastfm.service.lastfm.impl;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import org.springframework.stereotype.Service;

import com.kropsz.mslastfm.data.model.Collage;
import com.kropsz.mslastfm.data.model.UserData;
import com.kropsz.mslastfm.dto.LinkCollage;
import com.kropsz.mslastfm.dto.Request;
import com.kropsz.mslastfm.feign.LastfmClient;
import com.kropsz.mslastfm.service.lastfm.CollageService;
import com.kropsz.mslastfm.service.s3.S3Service;
import com.kropsz.mslastfm.service.user.UserService;
import com.kropsz.mslastfm.util.CollageConstructor;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CollageServiceImpl implements CollageService {

    private final LastfmClient lastfmClient;
    private final CollageConstructor collageConstructor;
    private final S3Service s3Service;
    private final UserService userService;

    @Override
    public LinkCollage createCollage(Request request) throws IOException {
        var user = userService.getUserData(request.getUser());

        var image = collageConstructor.drawImagesInGrid(lastfmClient.getTopAlbums(request), request.getLimit());

        var collage = saveCollage(image, user);
        return new LinkCollage(collage.getImageUrl().toString());
    }

    @Override
    public Collage saveCollage(BufferedImage image, UserData user) throws IOException {
        String fileName = "collage_" + user.getUser() + "_" + user.getCollages().size() + ".png";
        s3Service.uploadImage(image, fileName);
        return userService.addCollageToUser(user, getCollageUrl(fileName));

    }

    @Override
    public URL getCollageUrl(String fileName) {
        return s3Service.getPublicUrl(fileName);
    }

}