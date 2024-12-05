package com.th.mymap.location;

import com.th.mymap.common.MyFileUtils;
import com.th.mymap.entity.Location;
import com.th.mymap.entity.Picture;
import com.th.mymap.entity.User;
import com.th.mymap.location.model.LocationDto;
import com.th.mymap.location.model.LocationVo;
import com.th.mymap.location.model.PictureVo;
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
    public LocationVo getLocation(Long ilocation) {
        //본인이 맞는지 확인하는거 나중에 추가

        Location location = locationRepository.getReferenceById(ilocation);

        LocationVo resultVo = new LocationVo();
        resultVo.setIlocation(location.getIlocation());
        resultVo.setTitle(location.getTitle());
        resultVo.setDate(location.getDate());

        List<Picture> pictures = pictureRepository.findAllByLocation(location);
        List<PictureVo> pictureVos = pictures.stream().map(item -> PictureVo.builder()
                .ipicture(item.getIpicture())
                .thumbnails(item.getThumbnail())
                .build()
        ).toList();
        resultVo.setPictures(pictureVos);

        return resultVo;
    }

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

    @Transactional
    public String delPicture(Long ipicture) {
        Picture picture = pictureRepository.getReferenceById(ipicture);
        delPictures(picture);
        pictureRepository.delete(picture);
        return "삭제 완료";
    }

    @Transactional
    public String delLocation(Long ilocation) {
        Location location = locationRepository.getReferenceById(ilocation);
/*        List<Picture> pictures = pictureRepository.findAllByLocation(location);
        for (Picture picture : pictures) {
            delPictures(picture);
        }*/
        String path = "/location/" + location.getIlocation();
        myFileUtils.delDirTrigger(path);
        pictureRepository.deleteAllByLocation(location);
        locationRepository.delete(location);
        return "삭제 완료";
    }


    private void delPictures(Picture picture) {
        String[] fileNames = new String[]{picture.getPicture(), picture.getThumbnail()};
        for (String fileName : fileNames) {
            String filePath = "/location/" + picture.getLocation().getIlocation() + "/" + fileName;
            myFileUtils.delFile(filePath);
        }
    }

}
