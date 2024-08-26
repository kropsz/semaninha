package com.kropsz.msspotify.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kropsz.msspotify.dto.PlaylistDto;
import com.kropsz.msspotify.entity.enums.PlaylistType;
import com.kropsz.msspotify.exception.SpotifyApiException;
import com.kropsz.msspotify.service.spotify.PlaylistCreator;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.ExternalUrl;
import se.michaelthelin.spotify.model_objects.specification.Playlist;
import se.michaelthelin.spotify.requests.data.playlists.CreatePlaylistRequest;

@ExtendWith(MockitoExtension.class)
class CreatePlaylistTest {

    @Mock
    private SpotifyApi spotifyApi;

    @InjectMocks
    private PlaylistCreator playlistCreator;

    @Mock
    private CreatePlaylistRequest.Builder builder;

    @Mock
    private CreatePlaylistRequest createPlaylistRequest;

    @Mock
    private ExternalUrl externalUrl;

    @Mock
    private Playlist playlist;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should create a playlist successfully")
    void testCreatePlaylist_Success() throws Exception {
        String userId = "testUserId";
        PlaylistType playlistType = PlaylistType.USER_TRACKS;
        String playlistName = "Semaninha " + LocalDate.now();
        String playlistId = "playlistId";
        String spotifyUrl = "http://spotify.com/playlistId";

        Map<String, String> externalUrlsMap = new HashMap<>();
        externalUrlsMap.put("spotify", spotifyUrl);

        when(playlist.getExternalUrls()).thenReturn(externalUrl);
        when(spotifyApi.createPlaylist(userId, playlistName)).thenReturn(builder);
        when(builder.collaborative(false)).thenReturn(builder);
        when(builder.description("Playlist da semana")).thenReturn(builder);
        when(builder.public_(false)).thenReturn(builder);
        when(builder.build()).thenReturn(createPlaylistRequest);
        when(createPlaylistRequest.execute()).thenReturn(playlist);
        when(playlist.getId()).thenReturn(playlistId);
        when(externalUrl.getExternalUrls()).thenReturn(externalUrlsMap);

        PlaylistDto playlistDto = playlistCreator.createPlaylist(spotifyApi, userId, playlistType);

        assertEquals(playlistId, playlistDto.getId());
        assertEquals(spotifyUrl, playlistDto.getLink());
    }

    @Test
    @DisplayName("Should throw an exception when an error occurs")
    void testCreatePlaylist_Error() throws Exception {
        String userId = "testUserId";
        PlaylistType playlistType = PlaylistType.USER_TRACKS; 
        String playlistName = "Semaninha " + LocalDate.now();

        when(spotifyApi.createPlaylist(userId, playlistName)).thenReturn(builder);
        when(builder.collaborative(false)).thenReturn(builder);
        when(builder.description("Playlist da semana")).thenReturn(builder);
        when(builder.public_(false)).thenReturn(builder);
        when(builder.build()).thenReturn(createPlaylistRequest);
        when(createPlaylistRequest.execute()).thenThrow(new RuntimeException("Some error"));

        SpotifyApiException thrown = assertThrows(SpotifyApiException.class, () ->
        playlistCreator.createPlaylist(spotifyApi, userId, playlistType));

        assertEquals("Error creating playlist", thrown.getMessage());
    }
}