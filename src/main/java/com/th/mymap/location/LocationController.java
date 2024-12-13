package com.th.mymap.location;

import com.th.mymap.location.model.AllLocationVo;
import com.th.mymap.location.model.LocationDto;
import com.th.mymap.location.model.LocationVo;
import com.th.mymap.response.ApiResponse;
import com.th.mymap.response.ResVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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
    public ApiResponse<List<AllLocationVo>> getAllLocation() {
        return new ApiResponse<>(service.getAllLocation());
    }

    @GetMapping("/location/{ilocation}")
    public ApiResponse<LocationVo> getLocation(@PathVariable Long ilocation) {
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

    @GetMapping("location/pic/{fileName}")
    public ResponseEntity<InputStreamResource> downloadPicture(@RequestParam Long ilocation,
                                                               @PathVariable String fileName) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=" + fileName);
        return ResponseEntity.ok()
                .headers(headers)
                .body(service.downloadPicture(ilocation, fileName));
    }

    @DeleteMapping("/location/pic")
    public ApiResponse<?> delPicture(@RequestParam Long ipicture) {
        return new ApiResponse<>(service.delPicture(ipicture));
    }

/*    @GetMapping("/location/dummy")
    public ApiResponse<List<AllLocationVo>> getAllLocationDummy() {
        return new ApiResponse<>(service.getAllLocation());
    }*/
}
