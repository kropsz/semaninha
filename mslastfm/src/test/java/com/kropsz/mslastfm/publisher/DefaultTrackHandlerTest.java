package com.kropsz.mslastfm.publisher;

import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kropsz.mslastfm.dto.UserTrackList;
import com.kropsz.mslastfm.dto.album.Artist;
import com.kropsz.mslastfm.dto.track.Track;
import com.kropsz.mslastfm.publisher.handler.impl.DefaultTrackHandler;

@ExtendWith(MockitoExtension.class)
class DefaultTrackHandlerTest {

    @Mock
    private NotificationPublisher notificationPublisher;

    @InjectMocks
    private DefaultTrackHandler defaultTrackHandler;

    private UserTrackList userTrackList;

    @BeforeEach
    void setUp() {
        userTrackList = new UserTrackList("user123", List.of(new Track("testTrack", new Artist("testArtist"))), "7day");
    }

    @Test
    void handleTracksFromRedis_shouldNotify() {
        // Act
        defaultTrackHandler.handleTracksFromRedis(userTrackList);

        // Assert
        verify(notificationPublisher).notify(userTrackList);
    }

    @Test
    void handleTracksFromApi_shouldNotify() {
        // Act
        defaultTrackHandler.handleTracksFromApi(userTrackList);

        // Assert
        verify(notificationPublisher).notify(userTrackList);
    }
}