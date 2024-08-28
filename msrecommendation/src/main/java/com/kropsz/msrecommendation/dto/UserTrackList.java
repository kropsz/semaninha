package com.kropsz.msrecommendation.dto;

import com.kropsz.msrecommendation.entity.Tracks;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTrackList {

    private String user;
    private List<Tracks> tracks;
    private String period;

}
