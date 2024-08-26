package com.kropsz.msspotify.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class UserTrackList {

    private String user;
    private List<String> tracks;
    private String period;
    
}
