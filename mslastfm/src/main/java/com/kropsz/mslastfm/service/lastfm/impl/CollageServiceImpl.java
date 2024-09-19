package com.kropsz.mslastfm.service.lastfm.impl;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.springframework.stereotype.Service;

import com.kropsz.mslastfm.data.model.Collage;
import com.kropsz.mslastfm.data.model.UserData;
import com.kropsz.mslastfm.dto.LinkCollage;
import com.kropsz.mslastfm.dto.Request;
import com.kropsz.mslastfm.dto.track.Track;
import com.kropsz.mslastfm.feign.LastfmClient;
import com.kropsz.mslastfm.service.lastfm.CollageService;
import com.kropsz.mslastfm.service.lastfm.TrackService;
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
    private final TrackService trackService;

    @Override
    public LinkCollage createCollage(Request request) throws IOException {
        var user = userService.getUserData(request.getUser());

        var tracks = trackService.getRecentTracks(request);

        var image = collageConstructor.drawImagesInGrid(lastfmClient.getTopAlbums(request), request.getLimit());

        var collage = saveCollage(image, user, request.getPeriod(), tracks);
        return new LinkCollage(collage.getImageUrl().toString());
    }

    @Override
    public Collage saveCollage(BufferedImage image, UserData user, String period, List<Track> tracks) throws IOException {
        String fileName = "collage_" + user.getUser() + "_" + user.getCollages().size() + ".png";
        s3Service.uploadImage(image, fileName);
        return userService.addCollageToUser(user, getCollageUrl(fileName), period, tracks);

    }

    @Override
    public URL getCollageUrl(String fileName) {
        return s3Service.getPublicUrl(fileName);
    }

}