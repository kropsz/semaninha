package com.kropsz.mslastfm.publisher.handler.impl;

import org.springframework.stereotype.Component;

import com.kropsz.mslastfm.dto.UserTrackList;
import com.kropsz.mslastfm.publisher.NotificationPublisher;
import com.kropsz.mslastfm.publisher.handler.TrackHandler;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class DefaultTrackHandler implements TrackHandler {

    private final NotificationPublisher notificationPublisher;
    
    @Override
    public void handleTracksFromRedis(UserTrackList userTrackList) {
        notificationPublisher.notify(userTrackList);
    }

    @Override
    public void handleTracksFromApi(UserTrackList userTrackList) {
        notificationPublisher.notify(userTrackList);
    }
}