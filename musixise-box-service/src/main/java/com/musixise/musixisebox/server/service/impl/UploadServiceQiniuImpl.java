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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
    public Boolean upload(InputStream reponse, String saveFileName) {
        try {

            byte[] bt = readInputStream(reponse);
            Response res = uploadManager.put(bt, saveFileName, auth.uploadToken(IMG_BUCKET_NAME));
            logger.info(JSON.toJSONString(res));
            return true;
        } catch (QiniuException e) {
            logger.warn("upload exception", e.toString());
        } catch (IOException e) {
            logger.warn("upload exception", e.toString());
        }
        return false;
    }

    public static byte[] readInputStream(InputStream inStream) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        //每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len = 0;
        //使用一个输入流从buffer里把数据读取出来
        while( (len=inStream.read(buffer)) != -1 ){
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        //关闭输入流
        inStream.close();
        //把outStream里的数据写入内存
        return outStream.toByteArray();
    }
}
