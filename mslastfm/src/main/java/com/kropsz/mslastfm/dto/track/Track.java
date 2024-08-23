package com.kropsz.mslastfm.dto.track;


import com.kropsz.mslastfm.dto.album.Artist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class Track {
    private String name;
    private Artist artist;
}
