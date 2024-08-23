package com.kropsz.mslastfm.dto.album;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Image {
    private String size;
    
    @JsonProperty("#text")
    private String text;
}
