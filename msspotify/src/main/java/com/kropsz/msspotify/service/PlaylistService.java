package com.kropsz.msspotify.service;

import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.kropsz.msspotify.config.SpotifyConfig;
import com.kropsz.msspotify.entity.Tracks;
import com.kropsz.msspotify.entity.UserDetails;
import com.kropsz.msspotify.entity.enums.PlaylistType;
import com.kropsz.msspotify.service.spotify.PlaylistCreator;

import lombok.RequiredArgsConstructor;
import se.michaelthelin.spotify.SpotifyApi;

@Service
@RequiredArgsConstructor
public class PlaylistService {

    private final PlaylistCreator playlistCreator;
    private final SpotifyConfig spotifyConfig;
    private final RedisTemplate<String, Object> redisTemplate;

    public String buildPlaylistWithTracks(UserDetails userDetails, String user, PlaylistType playlistType) {
        List<Tracks> tracks = getTracksFromRedis(user, playlistType);

        SpotifyApi spotifyApi = getSpotifyApi(userDetails);

        var playlist = playlistCreator.createPlaylist(spotifyApi, userDetails.getId(), playlistType);

        String[] tracksId = playlistCreator.searchSpotifyTracksId(tracks, spotifyApi);

        playlistCreator.addTracksToPlaylist(spotifyApi, playlist.getId(), tracksId);
        deleteTracksFromRedis(user, playlistType);
        return playlist.getLink();

    }

    public SpotifyApi getSpotifyApi(UserDetails userDetails) {
        SpotifyApi spotifyApi = spotifyConfig.getSpotifyObject();
        spotifyApi.setAccessToken(userDetails.getAccessToken());
        spotifyApi.setRefreshToken(userDetails.getRefreshToken());
        return spotifyApi;
    }

    @SuppressWarnings("unchecked")
    public List<Tracks> getTracksFromRedis(String user, PlaylistType playlistType) {
        String redisKey = playlistType.getPrefix() + ":" + user;
        return (List<Tracks>) redisTemplate.opsForValue().get(redisKey);
    }

    public void deleteTracksFromRedis(String user, PlaylistType playlistType) {
        String redisKey = playlistType.getPrefix() + ":" + user;
        redisTemplate.delete(redisKey);
    }
}