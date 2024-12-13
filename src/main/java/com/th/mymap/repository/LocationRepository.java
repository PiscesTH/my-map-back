package com.th.mymap.repository;

import com.th.mymap.entity.Location;
import com.th.mymap.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findAllByUser(User user);
    Optional<Location> findByIlocationAndUser(Long ilocation, User user);
}
