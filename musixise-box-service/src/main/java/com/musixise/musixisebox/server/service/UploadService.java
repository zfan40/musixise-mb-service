package com.musixise.musixisebox.server.service;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by zhaowei on 2018/4/5.
 */
public abstract class UploadService {
        public abstract Boolean upload(MultipartFile file,String saveFileName);
        public abstract Boolean upload(byte[] bt, String saveFileName);
        public abstract Boolean upload(String content,  String saveFileName);
        public String buildFileName(String name) {
                return String.format("%s_%s", RandomStringUtils.randomAlphanumeric(8), name);
        }
}
