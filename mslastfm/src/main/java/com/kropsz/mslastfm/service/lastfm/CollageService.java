package com.kropsz.mslastfm.service.lastfm;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.awt.image.BufferedImage;

import com.kropsz.mslastfm.data.model.Collage;
import com.kropsz.mslastfm.data.model.UserData;
import com.kropsz.mslastfm.dto.LinkCollage;
import com.kropsz.mslastfm.dto.Request;
import com.kropsz.mslastfm.dto.track.Track;

public interface CollageService {

    public LinkCollage createCollage(Request request) throws IOException;

    public Collage saveCollage(BufferedImage image, UserData user, String period, List<Track> tracks) throws IOException;

    public URL getCollageUrl(String fileName);
}
