package com.musixise.musixisebox.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * Created by zhaowei on 2018/4/5.
 */
public class FileUtil {

     public static File multipartToFile(MultipartFile multipart) throws IllegalStateException, IOException {
        String filePath = System.getProperty("java.io.tmpdir") + System.getProperty("file.separator");
        File tmpFile = new File(filePath, multipart.getOriginalFilename());
        tmpFile.createNewFile();
        multipart.transferTo(tmpFile);
        return tmpFile;
    }
}
