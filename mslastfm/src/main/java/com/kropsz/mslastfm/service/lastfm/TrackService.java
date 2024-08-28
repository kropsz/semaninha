package com.kropsz.mslastfm.service.lastfm;

import java.util.List;

import com.kropsz.mslastfm.dto.Request;
import com.kropsz.mslastfm.dto.UserTrackList;
import com.kropsz.mslastfm.dto.track.Track;

public interface TrackService {

    public void getRecentTracks(Request request);

    public List<Track> fetchAndUpdateTracks(Request request, List<Track> existingTracks);

    public void saveOnRedis(List<Track> tracks, String user, String period);

    public UserTrackList getRecentTracksFromRedis(String user);

}
