package com.kropsz.msspotify.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.kropsz.msspotify.entity.Artist;
import com.kropsz.msspotify.entity.Tracks;
import com.kropsz.msspotify.service.spotify.PlaylistCreator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchTracksRequest;
import se.michaelthelin.spotify.model_objects.specification.Paging;

import java.util.Arrays;
import java.util.List;

class SearchTrackIdTest {

    @Mock
    private SpotifyApi spotifyApi;

    @Mock
    private SearchTracksRequest.Builder searchTracksRequestBuilder;

    @Mock
    private SearchTracksRequest searchTracksRequest;

    private PlaylistCreator playlistCreator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        playlistCreator = new PlaylistCreator();
    }

    @Test
    @DisplayName("Should return an array of tracks id")
    void testSearchSpotifyTracksId_NoTracksFound() throws Exception {
        List<Tracks> tracks = Arrays.asList(
                new Tracks("Track 1", new Artist("Artist 1")),
                new Tracks("Track 2", new Artist("Artist 2"))
        );

        Paging<Track> emptyTrackPaging = new Paging.Builder<Track>().setItems(new Track[]{}).build();

        when(spotifyApi.searchTracks(any(String.class))).thenReturn(searchTracksRequestBuilder);
        when(searchTracksRequestBuilder.limit(1)).thenReturn(searchTracksRequestBuilder);
        when(searchTracksRequestBuilder.offset(0)).thenReturn(searchTracksRequestBuilder);
        when(searchTracksRequestBuilder.build()).thenReturn(searchTracksRequest);
        when(searchTracksRequest.execute()).thenReturn(emptyTrackPaging);

        String[] tracksId = playlistCreator.searchSpotifyTracksId(tracks, spotifyApi);

        assertNull(tracksId[0]);
        assertNull(tracksId[1]);
    }

    @Test
    @DisplayName("Should return a exception when no tracks found")
    void testSearchSpotifyTracksId_ExceptionThrown() throws Exception {
        List<Tracks> tracks = Arrays.asList(
                new Tracks("Track 1", new Artist("Artist 1")),
                new Tracks("Track 2", new Artist("Artist 2"))
        );

        when(spotifyApi.searchTracks(any(String.class))).thenReturn(searchTracksRequestBuilder);
        when(searchTracksRequestBuilder.limit(1)).thenReturn(searchTracksRequestBuilder);
        when(searchTracksRequestBuilder.offset(0)).thenReturn(searchTracksRequestBuilder);
        when(searchTracksRequestBuilder.build()).thenReturn(searchTracksRequest);
        when(searchTracksRequest.execute()).thenThrow(new RuntimeException("API Error"));

        String[] tracksId = playlistCreator.searchSpotifyTracksId(tracks, spotifyApi);

        assertNull(tracksId[0]);
        assertNull(tracksId[1]);
    }
}