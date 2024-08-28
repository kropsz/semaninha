package com.kropsz.mslastfm.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kropsz.mslastfm.dto.Request;
import com.kropsz.mslastfm.dto.UserTrackList;
import com.kropsz.mslastfm.dto.album.Artist;
import com.kropsz.mslastfm.dto.track.TopTracks;
import com.kropsz.mslastfm.dto.track.Track;
import com.kropsz.mslastfm.dto.track.TrackResponse;
import com.kropsz.mslastfm.exception.RedisOperationException;
import com.kropsz.mslastfm.feign.LastfmClient;
import com.kropsz.mslastfm.publisher.handler.impl.DefaultTrackHandler;
import com.kropsz.mslastfm.service.lastfm.impl.TrackServiceImpl;

@ExtendWith(MockitoExtension.class)
class TrackServiceTest {

    @InjectMocks
    @Spy
    private TrackServiceImpl trackService;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private LastfmClient lastfmClient;

    @Mock
    private DefaultTrackHandler deafaultTrackHandler;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    private List<Track> tracks;

    @BeforeEach
    void setUp() {
        tracks = new ArrayList<>(Arrays.asList(new Track("testTrack", new Artist("testArtist")),
                new Track("testTrack2", new Artist("testArtist2"))));
    }

    @Test
    @DisplayName("Should return tracks from Redis if found")
    void testGetRecentTracks() {
        Request request = new Request("user", "7day", 2);
        UserTrackList userTracks = new UserTrackList("user", tracks, "7day");

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(trackService.getRecentTracksFromRedis("user")).thenReturn(userTracks);

        trackService.getRecentTracks(request);

        verify(deafaultTrackHandler).handleTracksFromRedis(userTracks);

    }

    @Test
    @DisplayName("Should return tracks when not found in Redis")
    void testGetRecentTracksFromApi() {
        Request request = new Request("user", "7day", 2);
        UserTrackList userTracks = new UserTrackList("user", tracks, "7day");
        TrackResponse response = new TrackResponse(new TopTracks(tracks));

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(trackService.getRecentTracksFromRedis("user")).thenReturn(null);
        when(lastfmClient.getTopTracks(request)).thenReturn(response);
        when(trackService.fetchAndUpdateTracks(request, null)).thenReturn(tracks);

        trackService.getRecentTracks(request);

        assertNotNull(tracks);

        verify(deafaultTrackHandler).handleTracksFromApi(userTracks);
    }

    @Test
    @DisplayName("Should save tracks on Redis")
    void testSaveOnRedis() throws JsonProcessingException {
        String user = "testUser";

        String period = "7days";
        UserTrackList userTracks = new UserTrackList(user, tracks, period);

        String jsonTracksWithPeriod = "{\"user\":\"exampleUser\"," +
                "\"tracks\":[{\"name\":\"testTrack\",\"artist\":{\"name\":\"testArtist\"}}," +
                "{\"name\":\"testTrack2\",\"artist\":{\"name\":\"testArtist2\"}}]," +
                "\"period\":\"7days\"}";

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(objectMapper.writeValueAsString(userTracks)).thenReturn(jsonTracksWithPeriod);

        trackService.saveOnRedis(tracks, user, period);

        verify(valueOperations).set(("recent_tracks:testUser"), (jsonTracksWithPeriod), (1L), (TimeUnit.HOURS));
    }

    @Test
    @DisplayName("Should throw RedisOperationException when save Redis fails")
    void testSaveOnRedisThrowsException() throws JsonProcessingException {

        when(objectMapper.writeValueAsString(any())).thenThrow(new RuntimeException());
        assertThrows(RedisOperationException.class, () -> trackService.saveOnRedis(tracks, "testUser", "7days"));
    }

    @Test
    @DisplayName("Should get tracks from Redis")
    void testGetRecentTracksFromRedis() throws JsonProcessingException {
        String user = "testUser";
        UserTrackList userTracks = new UserTrackList(user, tracks, "7day");

        String jsonTracksWithPeriod = "{\"user\":\"exampleUser\"," +
        "\"tracks\":[{\"name\":\"testTrack\",\"artist\":{\"name\":\"testArtist\"}}," +
        "{\"name\":\"testTrack2\",\"artist\":{\"name\":\"testArtist2\"}}]," +
        "\"period\":\"7days\"}";

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(redisTemplate.opsForValue().get("recent_tracks:testUser")).thenReturn(jsonTracksWithPeriod);
        when(objectMapper.readValue(jsonTracksWithPeriod, UserTrackList.class)).thenReturn(userTracks);

        assertEquals(userTracks, trackService.getRecentTracksFromRedis(user));

    }

    @Test
    @DisplayName("Should throw RedisOperationException when Redis fails")
    void testGetRecentTracksFromRedisThrowsException() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(redisTemplate.opsForValue().get(anyString())).thenThrow(new RuntimeException());
        assertThrows(RedisOperationException.class, () -> trackService.getRecentTracksFromRedis("testUser"));
    }

    @Test
    @DisplayName("Should remove tracks in fetchAndUpdateTracks")
    void testFetchAndUpdateTracks() {
        Request request = new Request("user", "7day", 2);
        List<Track> existingTracks = tracks;
        List<Track> newTracks = Arrays.asList(new Track("testTrack3", new Artist("testArtist3")),
                new Track("testTrack2", new Artist("testArtist2")));
        TrackResponse response = new TrackResponse(new TopTracks(newTracks));

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(lastfmClient.getTopTracks(request)).thenReturn(response);

        List<Track> updatedTracks = trackService.fetchAndUpdateTracks(request, existingTracks);

        assertTrue(updatedTracks.containsAll(newTracks));
        assertEquals(2, updatedTracks.size());
    }


}