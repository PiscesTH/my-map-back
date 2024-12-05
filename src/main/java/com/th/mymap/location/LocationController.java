package com.th.mymap.location;

import com.th.mymap.location.model.LocationDto;
import com.th.mymap.location.model.LocationVo;
import com.th.mymap.response.ApiResponse;
import com.th.mymap.response.ResVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LocationController {
    private final LocationService service;

    @GetMapping("/location")
    public ApiResponse<LocationVo> getLocation(@RequestParam Long ilocation) {
        return new ApiResponse<>(service.getLocation(ilocation));
    }

    @PostMapping("/location")
    public ApiResponse<ResVo> postLocation(@RequestPart("dto") LocationDto dto,
                                      @RequestPart("originals") List<MultipartFile> originals,
                                      @RequestPart("thumbnails") List<MultipartFile> thumbnails) {
        return new ApiResponse<>(service.postLocation(dto, originals, thumbnails));
    }

    @DeleteMapping("/location")
    public ApiResponse<?> delLocation(@RequestParam Long ilocation) {
        return new ApiResponse<>(service.delLocation(ilocation));
    }

    @DeleteMapping("/location/{ipicture}")
    public ApiResponse<?> delPicture(@PathVariable Long ipicture) {
        return new ApiResponse<>(service.delPicture(ipicture));
    }

}
