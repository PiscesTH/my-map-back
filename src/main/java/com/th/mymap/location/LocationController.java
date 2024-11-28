package com.th.mymap.location;

import com.th.mymap.location.model.LocationDto;
import com.th.mymap.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LocationController {
    private final LocationService service;

    @PostMapping("/record")
    public ApiResponse<?> postPicture(@RequestParam("location") LocationDto dto,
                                      @RequestParam("originals") List<MultipartFile> originals,
                                      @RequestParam("thumbnails") List<MultipartFile> thumbnails) {
        return null;
    }
}
