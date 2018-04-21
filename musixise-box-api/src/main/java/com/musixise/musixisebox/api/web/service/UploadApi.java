package com.musixise.musixisebox.api.web.service;

import com.musixise.musixisebox.api.result.MusixiseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by zhaowei on 2018/4/21.
 */
@Api(value = "上传文件", description = "上传文件", tags = "文件存储")
public interface UploadApi {

    @ApiOperation(value = "上传图片")
    MusixiseResponse uploadPic(@RequestBody @RequestParam("files")MultipartFile file);

    @ApiOperation(value = "上传音频")
    MusixiseResponse uploadAudio(Long uid, @RequestParam String data,
                                        @RequestParam String fname);

}
