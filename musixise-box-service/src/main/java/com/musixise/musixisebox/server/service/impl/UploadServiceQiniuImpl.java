package com.musixise.musixisebox.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.musixise.musixisebox.server.service.UploadService;
import com.musixise.musixisebox.server.utils.FileUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Created by zhaowei on 2018/4/5.
 */
@Component("uploadServiceQiniuImpl")
public class UploadServiceQiniuImpl extends UploadService {

    @Inject
    UploadManager uploadManager;

    @Inject
    Auth auth;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String IMG_BUCKET_NAME = "muixise-img";
    private static final String AUDIO_BUCKET_NAME = "muixise-audio";

    @Override
    public Boolean upload(MultipartFile file, String saveFileName) {
        try {
            Response res = uploadManager.put(FileUtil.multipartToFile(file), saveFileName,
                    auth.uploadToken(IMG_BUCKET_NAME));

            logger.info(JSON.toJSONString(res));
            return true;

        } catch (QiniuException e) {
            Response r = e.response;
            // 请求失败时打印的异常的信息
            logger.warn("upload exception", e.toString());

        } catch (IOException e) {
            e.printStackTrace();
            logger.warn("upload exception", e.toString());
        }

        return false;
    }

    @Override
    public Boolean upload(byte[] bt, String saveFileName) {
        try {
            Response res = uploadManager.put(bt, saveFileName, auth.uploadToken(AUDIO_BUCKET_NAME));
            logger.info(JSON.toJSONString(res));
            return true;
        } catch (QiniuException e) {
            logger.warn("upload exception", e.toString());
        } catch (IOException e) {
            logger.warn("upload exception", e.toString());
        }
        return false;
    }

    @Override
    public Boolean upload(String reponse, String saveFileName) {
        try {

           byte[] uploadBytes = reponse.getBytes("utf-8");
            ByteArrayInputStream byteInputStream=new ByteArrayInputStream(uploadBytes);
            Response res = uploadManager.put(byteInputStream, saveFileName, auth.uploadToken(IMG_BUCKET_NAME),null, null);
            logger.info(JSON.toJSONString(res));
            return true;
        } catch (QiniuException e) {
            logger.warn("upload exception", e.toString());
        } catch (IOException e) {
            logger.warn("upload exception", e.toString());
        }
        return false;
    }
}
