package com.th.mymap.common;

import com.th.mymap.exception.AuthErrorCode;
import com.th.mymap.exception.RestApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Component
public class MyFileUtils {
    private final String uploadPrefixPath;

    public MyFileUtils(@Value("${file.dir}") String uploadPrefixPath) {
        this.uploadPrefixPath = uploadPrefixPath;
    }

    public String transferTo(MultipartFile file, String path) {
        try {
            String originalFileName = file.getOriginalFilename();
            String ext = originalFileName.substring(originalFileName.lastIndexOf("."));
            String saveFileName = UUID.randomUUID() + ext;
            File dir = new File(uploadPrefixPath, String.valueOf(path));
            dir.mkdirs(); // 폴더 생성
            File saveFile = new File(dir.getPath(), saveFileName);
            log.info("path : {}", dir.getPath());
            file.transferTo(saveFile); // 파일 저장
            return saveFileName;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RestApiException(AuthErrorCode.IMAGE_UPLOAD_FAIL);
        }
    }

    public String[] saveOriginAndThumbnail(MultipartFile origin, MultipartFile thumbnail, String path) {
        try {
            String originalFileName = origin.getOriginalFilename();
            String ext = originalFileName.substring(originalFileName.lastIndexOf("."));
            String originFileName = UUID.randomUUID() + ext;
            String thumbnailFileName = "s_" + originFileName;
            File dir = new File(uploadPrefixPath, String.valueOf(path));
            dir.mkdirs(); // 폴더 생성
            log.info("path : {}", dir.getPath());
            File saveOriginFile = new File(dir.getPath(), originFileName);
            origin.transferTo(saveOriginFile); // 파일 저장
            File saveThumbnailFile = new File(dir.getPath(), thumbnailFileName);
            thumbnail.transferTo(saveThumbnailFile);
            return new String[]{originFileName, thumbnailFileName};
        } catch (Exception e) {
            e.printStackTrace();
            throw new RestApiException(AuthErrorCode.IMAGE_UPLOAD_FAIL);
        }
    }

    public void delDirTrigger(String relativePath) {
        delDir(uploadPrefixPath + relativePath);
    }

    public void delFile2(String filePath) {//파일 삭제
        File file = new File(uploadPrefixPath + filePath);
        if(file.exists()) {
            file.delete();
        }
    }

    public void delFile(String filePath) {
        Path path = Paths.get(uploadPrefixPath + filePath);
        log.info(uploadPrefixPath + filePath);
        try {
            // 파일 삭제
            Files.deleteIfExists(path);
        } catch (IOException e) {
            log.info("파일 삭제 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    public void delDir(String dirPath) {
        File dir = new File(dirPath);
        int result = 0;

        if (dir.exists()) { // exists - 디렉토리 존재 여부 확인
            File[] files = dir.listFiles(); // 해당 디렉토리에 있는 모든 파일을 리스트에 담음

            // files에 담긴 파일이 디렉토리면 delDir을 다시 호출해(재귀함수) 해당 디렉토리의 파일을 다시 리스트에 담음
            for (File file : files) {
                if (file.isDirectory()) {
                    delDir(file.getAbsolutePath());
                } else {
                    file.delete(); // 하위 디렉토리에 파일만 있을 경우 해당 파일 삭제
                }
            }
            dir.delete(); // 마지막으로 디렉토리 삭제
        }
    }
}
