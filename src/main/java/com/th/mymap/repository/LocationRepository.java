package com.th.mymap.repository;

import com.th.mymap.entity.Location;
import com.th.mymap.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findAllByUser(User user);
}
