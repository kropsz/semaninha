package com.kropsz.msspotify.service.spotify;

import com.kropsz.msspotify.dto.PlaylistDto;
import com.kropsz.msspotify.entity.Tracks;
import com.kropsz.msspotify.entity.enums.PlaylistType;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Track;


import java.util.List;

public interface SpotifyPlaylist {

    void addTracksToPlaylist(SpotifyApi spotifyApi, String playlistId, String[] tracksId);

    PlaylistDto createPlaylist(SpotifyApi spotifyApi, String userId, PlaylistType playlistType);

    String[] searchSpotifyTracksId(List<Tracks> tracks, SpotifyApi spotifyApi);

    String extractTrackId(Paging<Track> trackPaging);

}