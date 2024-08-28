package com.kropsz.msspotify.dto;

import java.util.List;

import com.kropsz.msspotify.entity.Tracks;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTrackList {

    private String user;
    private List<Tracks> tracks;
    private String period;

}
