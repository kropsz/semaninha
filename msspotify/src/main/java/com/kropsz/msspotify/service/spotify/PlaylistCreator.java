package com.kropsz.msspotify.service.spotify;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

import com.kropsz.msspotify.dto.PlaylistDto;
import com.kropsz.msspotify.entity.Tracks;
import com.kropsz.msspotify.entity.enums.PlaylistType;
import com.kropsz.msspotify.exception.SpotifyApiException;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Playlist;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.data.playlists.AddItemsToPlaylistRequest;
import se.michaelthelin.spotify.requests.data.playlists.CreatePlaylistRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchTracksRequest;

@Component
public class PlaylistCreator implements SpotifyPlaylist {

    @Override
    public void addTracksToPlaylist(SpotifyApi spotifyApi, String playlistId, String[] tracksId) {
        try {
            AddItemsToPlaylistRequest addItemsToPlaylistRequest = spotifyApi.addItemsToPlaylist(playlistId, tracksId)
                    .build();
            addItemsToPlaylistRequest.execute();
        } catch (Exception e) {
            throw new SpotifyApiException("Error adding tracks to playlist");
        }
    }

    @Override
    public PlaylistDto createPlaylist(SpotifyApi spotifyApi, String userId, PlaylistType playlistType) {
        String playlistName = getPlaylistName(playlistType);
        String playlistDescription = getPlaylistDescription(playlistType);

        try {
            CreatePlaylistRequest createPlaylistRequest = spotifyApi.createPlaylist(userId, playlistName)
                    .collaborative(false)
                    .description(playlistDescription)
                    .public_(false)
                    .build();
            Playlist playlist = createPlaylistRequest.execute();
            String spotifyUrl = playlist.getExternalUrls().getExternalUrls().get("spotify");
            return new PlaylistDto(playlist.getId(), spotifyUrl);
        } catch (Exception e) {
            throw new SpotifyApiException("Error creating playlist");
        }
    }

    @Override
    public String[] searchSpotifyTracksId(List<Tracks> tracks, SpotifyApi spotifyApi) {
        String[] tracksId = new String[tracks.size()];
        int index = 0;

        for (Tracks track : tracks) {
            tracksId[index] = searchTrackId(track, spotifyApi);
            index++;
        }

        return tracksId;
    }

    @Override
    public String extractTrackId(Paging<Track> trackPaging) {
        if (trackPaging.getItems().length > 0) {
            return trackPaging.getItems()[0].getUri();
        }
        return null;
    }

    private String searchTrackId(Tracks track, SpotifyApi spotifyApi) {
        String searchQuery = track.getName() + " " + track.getArtist().getName();

        try {
            SearchTracksRequest searchTracksRequest = spotifyApi.searchTracks(searchQuery)
                    .limit(1)
                    .offset(0)
                    .build();

            Paging<Track> trackPaging = searchTracksRequest.execute();
            return extractTrackId(trackPaging);
        } catch (Exception e) {
            return null;
        }
    }

    private String getPlaylistName(PlaylistType playlistType) {
        return switch (playlistType) {
            case USER_TRACKS -> "Semaninha " + LocalDate.now();
            case SUGGESTION_TRACKS -> "Suggestion by Semaninha";
            default -> throw new IllegalArgumentException("Tipo de track não suportado: " + playlistType);
        };
    }

    private String getPlaylistDescription(PlaylistType playlistType) {
        return switch (playlistType) {
            case USER_TRACKS -> "Playlist da semana";
            case SUGGESTION_TRACKS -> "Playlist com as recomendações da semana";
            default -> throw new IllegalArgumentException("Tipo de track não suportado: " + playlistType);
        };
    }
}