package com.th.mymap.location;

import com.th.mymap.common.MyFileUtils;
import com.th.mymap.entity.Location;
import com.th.mymap.entity.Picture;
import com.th.mymap.entity.User;
import com.th.mymap.location.model.LocationDto;
import com.th.mymap.repository.LocationRepository;
import com.th.mymap.repository.PictureRepository;
import com.th.mymap.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final PictureRepository pictureRepository;

    @Transactional
    public ResVo postLocation(LocationDto dto,
                              List<MultipartFile> originals,
                              List<MultipartFile> thumbnails) {
        //임시 user
        User user = userRepository.getReferenceById(1L);

        Location location = new Location();
        location.setUser(user);
        location.setTitle(dto.getTitle());
        location.setLng(dto.getLng());
        location.setLat(dto.getLat());
        location.setDate(dto.getDate());
        locationRepository.save(location);

        String target = "/location/" + location.getIlocation();
/*        for (MultipartFile original : originals) {
            String savedFileName = myFileUtils.transferTo(original, target);
            Picture picture = new Picture();
            picture.setLocation(location);
            picture.setPicture(savedFileName);
            pictureRepository.save(picture);
        }*/
        for (int i = 0; i < originals.size(); i++) {
            String[] savedFileName = myFileUtils.saveOriginAndThumbnail(originals.get(i), thumbnails.get(i), target);
            Picture picture = new Picture();
            picture.setLocation(location);
            picture.setPicture(savedFileName[0]);
            picture.setThumbnail(savedFileName[1]);
            pictureRepository.save(picture);
        }
        return new ResVo(location.getIlocation().intValue());
    }
}
