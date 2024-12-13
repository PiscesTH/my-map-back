package com.th.mymap.location;

import com.th.mymap.common.MyFileUtils;
import com.th.mymap.entity.Location;
import com.th.mymap.entity.Picture;
import com.th.mymap.entity.User;
import com.th.mymap.exception.CommonErrorCode;
import com.th.mymap.exception.RestApiException;
import com.th.mymap.location.model.AllLocationVo;
import com.th.mymap.location.model.LocationDto;
import com.th.mymap.location.model.LocationVo;
import com.th.mymap.location.model.PictureVo;
import com.th.mymap.repository.LocationRepository;
import com.th.mymap.repository.PictureRepository;
import com.th.mymap.repository.UserRepository;
import com.th.mymap.response.ResVo;
import com.th.mymap.security.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationService {
    private final MyFileUtils myFileUtils;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final PictureRepository pictureRepository;
    private final AuthenticationFacade authenticationFacade;

    @Transactional
    public LocationVo getLocation(Long ilocation) {
        User user = getLoginedUser();
        Optional<Location> opt = locationRepository.findByIlocationAndUser(ilocation, user);
        if (opt.isEmpty()) {
            throw new RestApiException(CommonErrorCode.BAD_REQUEST);
        }
        log.info("iuser : {}",user.getIuser());
        Location location = opt.get();
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
    public List<AllLocationVo> getAllLocation() {
        User user = getLoginedUser();
        return getAllLocationProc(user);
    }

    @Transactional
    public ResVo postLocation(LocationDto dto,
                              List<MultipartFile> originals,
                              List<MultipartFile> thumbnails) {
        //임시 user
        User user = getLoginedUser();

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
    public String delLocation(Long ilocation) {
        User user = getLoginedUser();
        Optional<Location> opt = locationRepository.findByIlocationAndUser(ilocation, user);
        if (opt.isEmpty()) {
            throw new RestApiException(CommonErrorCode.BAD_REQUEST);
        }
        Location location = opt.get();
        String path = "/location/" + location.getIlocation();
        myFileUtils.delDirTrigger(path);
        pictureRepository.deleteAllByLocation(location);
        locationRepository.delete(location);
        return "삭제 완료";
    }


    @Transactional
    public String delPicture(Long ipicture) {
        Picture picture = pictureRepository.getReferenceById(ipicture);
        delPictures(picture);
        pictureRepository.delete(picture);
        return "삭제 완료";
    }

    //이미지 다운로드
    public InputStreamResource downloadPicture(Long ilocation, String fileName) {
        return myFileUtils.downloadFile(ilocation, fileName);
    }

    //이하부턴 내부에서 사용하는 메서드
    private void delPictures(Picture picture) {
        String[] fileNames = new String[]{picture.getPicture(), picture.getThumbnail()};
        for (String fileName : fileNames) {
            String filePath = "/location/" + picture.getLocation().getIlocation() + "/" + fileName;
            myFileUtils.delFile(filePath);
        }
    }

    private User getLoginedUser() {
        return userRepository.getReferenceById(authenticationFacade.getLoginUserPk());
    }

    //user 정보를 받아 등록된 모든 위치를 반환(더미데이터 뿌릴때 코드가 중복되서 분리했는데, 다른 방식으로 해결됨. 그래도 일단은 분리시켜놓음)
    private List<AllLocationVo> getAllLocationProc(User user) {
        List<Location> locationList = locationRepository.findAllByUser(user);
        List<AllLocationVo> resultList = locationList.stream().map(item -> {
            AllLocationVo vo = new AllLocationVo();
            vo.setIlocation(item.getIlocation());
            vo.setTitle(item.getTitle());
            vo.setDate(item.getDate());
            vo.getLatlng().put("lat", item.getLat());
            vo.getLatlng().put("lng", item.getLng());
            return vo;
        }).toList();
        return resultList;
    }

    /*@Transactional
    public List<AllLocationVo> getAllLocationDummy() {
        User user = userRepository.getReferenceById(1L);
        return getAllLocationProc(user);
    }*/
}
