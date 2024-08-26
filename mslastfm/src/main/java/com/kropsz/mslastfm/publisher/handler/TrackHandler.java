package com.kropsz.mslastfm.publisher.handler;

import com.kropsz.mslastfm.dto.UserTrackList;

public interface TrackHandler {
    void handleTracksFromRedis(UserTrackList userTrackList);
    void handleTracksFromApi(UserTrackList userTrackList);
}