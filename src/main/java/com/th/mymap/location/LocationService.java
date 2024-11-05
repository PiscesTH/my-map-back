package com.th.mymap.location;

import com.th.mymap.common.MyFileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationService {
    private final MyFileUtils myFileUtils;
}
