package com.musixise.musixisebox.manager;

import com.musixise.musixisebox.service.UploadService;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * 第三方/异常/缓存处理
 * Created by zhaowei on 2018/4/5.
 */
@Component
public class UploaderManager {

    private UploadService uploadService;

    /**
     * 生成唯一文件名
     * @param name
     * @return
     */
    public String buildFileName(String name) {
        return uploadService.buildFileName(name);
    }

    /**
     * 上传图片
     * @param file
     * @param fileName
     * @return
     */
    public Boolean upload(MultipartFile file, String fileName) {
        return uploadService.upload(file, fileName);
    }

    /**
     * 上传音频文件
     * @param bt
     * @param fileName
     * @return
     */
    public Boolean upload(byte[] bt, String fileName) {
        return uploadService.upload(bt, fileName);
    }

    public void setUploadService(UploadService uploadService) {
        this.uploadService = uploadService;
    }
}
