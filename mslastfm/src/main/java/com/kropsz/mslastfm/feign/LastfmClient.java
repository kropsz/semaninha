package com.kropsz.mslastfm.feign;

import com.kropsz.mslastfm.config.LastFmKeyConfig;
import com.kropsz.mslastfm.dto.Request;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.kropsz.mslastfm.dto.album.AlbumsResponse;
import com.kropsz.mslastfm.dto.track.TrackResponse;

@FeignClient(name = "LastFM", url = "https://ws.audioscrobbler.com/2.0", configuration = LastFmKeyConfig.class)
public interface LastfmClient {

        @GetMapping("/?method=user.gettopalbums&user={user}&period={period}&limit={limit}&format=json")
        AlbumsResponse getTopAlbums(@PathVariable String user,
                        @PathVariable String period,
                        @PathVariable int limit);

        @GetMapping("/?method=user.gettoptracks&user={user}&period={period}&limit={limit}&format=json")
        TrackResponse getTopTracks(@PathVariable String user,
                        @PathVariable String period,
                        @PathVariable int limit);

        default AlbumsResponse getTopAlbums(Request request) {
                return getTopAlbums(request.getUser(), request.getPeriod(), request.getLimit());
        }

        default TrackResponse getTopTracks(Request request) {
                return getTopTracks(request.getUser(), request.getPeriod(), request.getLimit());
        }

}