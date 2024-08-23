package com.kropsz.mslastfm.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request {

    private String user;

    @Pattern(regexp = "overall|7day|1month", message = "period must be one of: overall, 7day, 1month")
    private String period;

    @Max(25)
    private int limit;
}