package com.th.mymap.location.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class LocationVo {
    private Long ilocation;
    private String title;
    private LocalDateTime date;
    private List<PictureVo> pictures;
}
