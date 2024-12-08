package com.th.mymap.location.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
public class AllLocationVo {
    private Long ilocation;
    private String title;
    private LocalDateTime date;
    private Map<String, Double> latlng = new HashMap<>();
}
