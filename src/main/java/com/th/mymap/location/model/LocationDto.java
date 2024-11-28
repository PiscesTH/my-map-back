package com.th.mymap.location.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LocationDto {
    private String title;
    private Double lat;
    private Double lng;
    private LocalDateTime date;
}
