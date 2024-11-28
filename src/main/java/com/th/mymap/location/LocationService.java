package com.th.mymap.location;

import com.th.mymap.common.MyFileUtils;
import com.th.mymap.entity.Location;
import com.th.mymap.entity.Picture;
import com.th.mymap.entity.User;
import com.th.mymap.location.model.LocationDto;
import com.th.mymap.repository.LocationRepository;
import com.th.mymap.response.ResVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationService {
    private final MyFileUtils myFileUtils;
    private final LocationRepository locationRepository;

    private final Long iuser = 1L;

    @Transactional
    public ResVo postLocation(LocationDto dto,
                              List<MultipartFile> originals,
                              List<MultipartFile> thumbnails) {
        //임시 user
        User user = new User();
        user.setIuser(iuser);
        user.setNm("태하");
        user.setUid("xogk");
        user.setUpw("111");
        user.setEmail("asd@naver.com");

        Location location = new Location();
        location.setUser(user);
        location.setTitle(dto.getTitle());
        location.setLng(dto.getLng());
        location.setLat(dto.getLat());
        location.setDate(dto.getDate());
        locationRepository.save(location);

        String target = "/location/" +location.getIlocation();
        Picture picture = new Picture();
        picture.setLocation(location);
        for (MultipartFile original : originals) {
            String savedFileName = myFileUtils.transferTo(original, target);

        }

        return new ResVo(1);
    }
}
