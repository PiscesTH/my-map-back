package com.th.mymap.repository;

import com.th.mymap.entity.Location;
import com.th.mymap.entity.Picture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PictureRepository extends JpaRepository<Picture, Long> {
    List<Picture> findAllByLocation(Location location);
    void deleteAllByLocation(Location location);
}
