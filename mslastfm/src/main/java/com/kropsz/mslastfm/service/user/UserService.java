package com.kropsz.mslastfm.service.user;

import java.net.URL;
import java.util.List;

import com.kropsz.mslastfm.data.model.Collage;
import com.kropsz.mslastfm.data.model.UserData;
import com.kropsz.mslastfm.dto.track.Track;

public interface UserService {

    UserData getUserData(String username);

    Collage addCollageToUser(UserData user, URL fileName, String period, List<Track> tracks);

    List<Collage> getCollages(String username);
}
