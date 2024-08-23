package com.kropsz.mslastfm.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Pattern;

public record Request(String user, 

    @Pattern(regexp = "overall|7day|1month", 
             message = "period must be one of: overall, 7day, 1month")
    String period, 
    @Max(25)
    int limit) {

}