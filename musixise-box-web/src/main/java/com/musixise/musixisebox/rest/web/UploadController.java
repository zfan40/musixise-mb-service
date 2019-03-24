package com.musixise.musixisebox.rest.web;

import com.musixise.musixisebox.api.enums.ExceptionMsg;
import com.musixise.musixisebox.api.result.MusixiseResponse;
import com.musixise.musixisebox.api.web.service.UploadApi;
import com.musixise.musixisebox.server.aop.AppMethod;
import com.musixise.musixisebox.server.aop.MusixiseContext;
import com.musixise.musixisebox.server.manager.UploaderManager;
import com.musixise.musixisebox.server.service.UploadService;
import com.musixise.musixisebox.server.service.WorkService;
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

    @Resource UploadService uploadServiceQiniuImpl;

    @Resource WorkService workService;

    /**
     * 上传图片
     * @param file
     * @return
     */
    @RequestMapping(value = "/picture/uploadPic", method = RequestMethod.POST)
    @AppMethod
    @Override
    public MusixiseResponse uploadPic(@RequestBody @RequestParam("files")MultipartFile file) {

        uploaderManager.setUploadService(uploadServiceQiniuImpl);

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

        uid = MusixiseContext.getCurrentUid();
        uploaderManager.setUploadService(uploadServiceQiniuImpl);

        byte[] bt = null;
        try {
            sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
            //data:;base64,aSBhbSBhIGJsb2I=
            //data:audio/midi;charset=binary;base64,
            data = data.replaceAll("data:audio/midi;charset=binary;base64,", "");
            bt = decoder.decodeBuffer( data);
            //value = new String(bt, "UTF-8");
            //生成文件名
            String fileName = uploaderManager.buildFileName(fname);
            //上传文件
            if (uploaderManager.upload(bt, fileName)) {
                String audioFullName = FileUtil.getAudioFullName(fileName);
                //分析midi 并保存
                workService.saveMidiFile(bt, audioFullName);

                return new MusixiseResponse<>(ExceptionMsg.SUCCESS, audioFullName);
            } else {
                return new MusixiseResponse<>(ExceptionMsg.UPLOAD_ERROR);
            }

        } catch (Exception e) {
            return new MusixiseResponse<>(ExceptionMsg.UPLOAD_ERROR);
        }
    }
}
