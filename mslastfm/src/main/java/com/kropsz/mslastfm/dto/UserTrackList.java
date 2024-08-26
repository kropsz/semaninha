package com.kropsz.mslastfm.dto;

import java.util.List;

import com.kropsz.mslastfm.dto.track.Track;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTrackList {

    private String user;
    private List<Track> tracks;
    private String period;

}