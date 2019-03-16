package com.musixise.musixisebox.rest.web;

import com.alibaba.fastjson.JSONObject;
import com.musixise.musixisebox.api.enums.ExceptionMsg;
import com.musixise.musixisebox.api.result.MusixiseResponse;
import com.musixise.musixisebox.api.web.service.WeChatApi;
import com.musixise.musixisebox.server.aop.AppMethod;
import com.musixise.musixisebox.server.manager.UploaderManager;
import com.musixise.musixisebox.server.manager.WeChatManager;
import com.musixise.musixisebox.server.service.UploadService;
import com.musixise.musixisebox.server.utils.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.Map;

/**
 * Created by zhaowei on 2018/4/5.
 */
@RestController
@RequestMapping("/api/v1/wechat")
public class WeChatController implements WeChatApi {

    private @Resource WeChatManager weChatManager;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    UploaderManager uploaderManager;

    @Resource
    UploadService uploadServiceQiniuImpl;

    @Value("${spring.social.wechat.clientId}") String weixinAppId;
    @Value("${spring.social.wechat.clientSecret}") String weixinAppSecret;

    @RequestMapping(value = "/getTicket", method = RequestMethod.POST)
    @AppMethod
    @Override
    public MusixiseResponse<JSONObject> getJsTicket(@RequestParam(value = "url", defaultValue = "") String url) {

        Map<String, String> ret = weChatManager.getJsTicket(weixinAppId, weixinAppSecret, url);

        if (ret != null && ret.containsKey("jsapi_ticket")) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("nonceStr", ret.get("nonceStr"));
            jsonObject.put("jsapiTicket", ret.get("jsapi_ticket"));
            jsonObject.put("timestamp", ret.get("timestamp"));
            jsonObject.put("url", ret.get("url"));
            jsonObject.put("signature", ret.get("signature"));
            jsonObject.put("appId", weixinAppId);

            return new MusixiseResponse<>(ExceptionMsg.SUCCESS, jsonObject);
        } else {
            return new MusixiseResponse<>(ExceptionMsg.FAILED);
        }
    }

    @RequestMapping(value = "/saveMedia", method = RequestMethod.POST)
    @AppMethod
    @Override
    public MusixiseResponse<String> saveMedia(@RequestParam(value = "media_id", defaultValue = "") String mediaId) {

        uploaderManager.setUploadService(uploadServiceQiniuImpl);

        InputStream media = weChatManager.getMedia(mediaId);

        //生成文件名
        String fileName = uploaderManager.buildFileName(mediaId + ".jpg");
        //上传文件
        if (uploaderManager.upload(media, fileName)) {
            return new MusixiseResponse<>(ExceptionMsg.SUCCESS, FileUtil.getImageFullName(fileName));
        } else {
            return new MusixiseResponse<>(ExceptionMsg.UPLOAD_ERROR);
        }


    }
}
