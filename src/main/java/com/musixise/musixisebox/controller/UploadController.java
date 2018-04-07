package com.musixise.musixisebox.controller;

import com.musixise.musixisebox.aop.AppMethod;
import com.musixise.musixisebox.domain.result.ExceptionMsg;
import com.musixise.musixisebox.domain.result.MusixiseResponse;
import com.musixise.musixisebox.manager.UploaderManager;
import com.musixise.musixisebox.service.impl.UploadServiceQiniuImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * Created by zhaowei on 2018/4/5.
 */
@RestController
@Api(value = "上传文件", description = "上传文件")
@RequestMapping("/api")
public class UploadController {

    @Resource UploaderManager uploaderManager;

    /**
     * 上传图片
     * @param file
     * @return
     */
    @RequestMapping(value = "/picture/uploadPic", method = RequestMethod.POST)
    @ApiOperation(value = "上传图片")
    @AppMethod
    public MusixiseResponse uploadPic(@RequestBody @RequestParam("files")MultipartFile file) {

        uploaderManager.setUploadService(new UploadServiceQiniuImpl());

        //生成文件名
        String fileName = uploaderManager.buildFileName(file.getOriginalFilename());
        //上传文件
        if (uploaderManager.upload(file, fileName)) {
            return new MusixiseResponse(ExceptionMsg.SUCCESS, fileName);
        } else {
            return new MusixiseResponse(ExceptionMsg.UPLOAD_ERROR);
        }
    }


    /**
     * 上传音频
     * @param data
     * @param fname
     * @return
     */
    @RequestMapping(value = "uploadAudio", method = RequestMethod.POST)
    @ApiOperation(value = "上传音频")
    @AppMethod(isLogin = true)
    public MusixiseResponse uploadAudio(Long uid, @RequestBody @RequestParam String data,
                                        @RequestParam String fname) {

        uploaderManager.setUploadService(new UploadServiceQiniuImpl());

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
                return new MusixiseResponse(ExceptionMsg.SUCCESS, fileName);
            } else {
                return new MusixiseResponse(ExceptionMsg.UPLOAD_ERROR);
            }

        } catch (Exception e) {
            return new MusixiseResponse(ExceptionMsg.UPLOAD_ERROR);
        }
    }
}
