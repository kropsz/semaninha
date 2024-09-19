package com.kropsz.mslastfm.data.model;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;

import com.kropsz.mslastfm.dto.track.Track;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Collage {

    private URL imageUrl;
    private LocalDate date;
    private String period;
    private List<Track> tracks;

}
