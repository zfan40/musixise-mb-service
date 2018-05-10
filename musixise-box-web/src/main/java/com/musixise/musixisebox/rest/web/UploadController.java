package com.musixise.musixisebox.rest.web;

import com.musixise.musixisebox.server.aop.AppMethod;
import com.musixise.musixisebox.api.enums.ExceptionMsg;
import com.musixise.musixisebox.api.result.MusixiseResponse;
import com.musixise.musixisebox.api.web.service.UploadApi;
import com.musixise.musixisebox.server.manager.UploaderManager;
import com.musixise.musixisebox.server.service.impl.UploadServiceQiniuImpl;
import com.musixise.musixisebox.server.utils.FileUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * Created by zhaowei on 2018/4/5.
 */
@RestController
@RequestMapping("/api/v1")
public class UploadController implements UploadApi {

    @Resource UploaderManager uploaderManager;

    @Resource UploadServiceQiniuImpl uploadServiceQiniu;

    /**
     * 上传图片
     * @param file
     * @return
     */
    @RequestMapping(value = "/picture/uploadPic", method = RequestMethod.POST)
    @AppMethod
    @Override
    public MusixiseResponse uploadPic(@RequestBody @RequestParam("files")MultipartFile file) {

        uploaderManager.setUploadService(new UploadServiceQiniuImpl());

        //生成文件名
        String fileName = uploaderManager.buildFileName(file.getOriginalFilename());
        //上传文件
        if (uploaderManager.upload(file, fileName)) {
            return new MusixiseResponse<>(ExceptionMsg.SUCCESS, FileUtil.getImageFullName(fileName));
        } else {
            return new MusixiseResponse<>(ExceptionMsg.UPLOAD_ERROR);
        }
    }


    /**
     * 上传音频
     * @param data
     * @param fname
     * @return
     */
    @RequestMapping(value = "uploadAudio", method = RequestMethod.POST)
    @AppMethod(isLogin = true)
    @Override
    public MusixiseResponse uploadAudio(Long uid, @RequestParam String data,
                                        @RequestParam String fname) {

        uploaderManager.setUploadService(uploadServiceQiniu);

        byte[] bt = null;
        try {
            sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
            //data:;base64,aSBhbSBhIGJsb2I=
            data = data.replaceAll("data:;base64,", "");
            bt = decoder.decodeBuffer( data);
            //value = new String(bt, "UTF-8");
            //生成文件名
            String fileName = uploaderManager.buildFileName(fname);
            //上传文件
            if (uploaderManager.upload(bt, fileName)) {
                return new MusixiseResponse<>(ExceptionMsg.SUCCESS, FileUtil.getAudioFullName(fileName));
            } else {
                return new MusixiseResponse<>(ExceptionMsg.UPLOAD_ERROR);
            }

        } catch (Exception e) {
            return new MusixiseResponse<>(ExceptionMsg.UPLOAD_ERROR);
        }
    }
}
