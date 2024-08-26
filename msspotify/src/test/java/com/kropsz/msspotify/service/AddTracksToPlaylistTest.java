package com.kropsz.msspotify.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kropsz.msspotify.exception.SpotifyApiException;
import com.kropsz.msspotify.service.spotify.PlaylistCreator;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.requests.data.playlists.AddItemsToPlaylistRequest;

@ExtendWith(MockitoExtension.class)
class AddTracksToPlaylistTest {

    @Mock
    private SpotifyApi spotifyApi;

    @InjectMocks
    private PlaylistCreator playlistCreator;

    @Mock
    private AddItemsToPlaylistRequest.Builder builder;

    @Mock
    private AddItemsToPlaylistRequest addItemsToPlaylistRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddTracksToPlaylist_Success() throws Exception {
        String playlistId = "testPlaylistId";
        String[] tracksId = {"track1", "track2"};

        when(spotifyApi.addItemsToPlaylist(playlistId, tracksId)).thenReturn(builder);
        when(builder.build()).thenReturn(addItemsToPlaylistRequest);
        when(addItemsToPlaylistRequest.execute()).thenReturn(null);

        playlistCreator.addTracksToPlaylist(spotifyApi, playlistId, tracksId);

        verify(addItemsToPlaylistRequest).execute();
    }

    @Test
    void testAddTracksToPlaylist_Error() throws Exception {
        String playlistId = "testPlaylistId";
        String[] tracksId = {"track1", "track2"};

        when(spotifyApi.addItemsToPlaylist(playlistId, tracksId)).thenReturn(builder);
        when(builder.build()).thenReturn(addItemsToPlaylistRequest);
        when(addItemsToPlaylistRequest.execute()).thenThrow(new RuntimeException("Some error"));

        var thrown = assertThrows(SpotifyApiException.class, () ->
        playlistCreator.addTracksToPlaylist(spotifyApi, playlistId, tracksId));

        assertEquals("Error adding tracks to playlist", thrown.getMessage());
    }



}