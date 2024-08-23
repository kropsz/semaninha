package com.kropsz.mslastfm.dto.album;
import java.util.List;

import lombok.Data;

@Data
public class Album {

    private Artist artist;
    private List<Image> image;
    private String name;
}
