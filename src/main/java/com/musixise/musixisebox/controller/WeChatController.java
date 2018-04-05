package com.musixise.musixisebox.controller;

import com.alibaba.fastjson.JSONObject;
import com.musixise.musixisebox.aop.AppMethod;
import com.musixise.musixisebox.domain.result.ExceptionMsg;
import com.musixise.musixisebox.domain.result.ResponseData;
import com.musixise.musixisebox.manager.WeChatManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by zhaowei on 2018/4/5.
 */
@RestController
@RequestMapping("/wechat")
public class WeChatController {

    @Resource WeChatManager weChatManager;

    @Value("${spring.social.wechat.clientId}") String weixinAppId;
    @Value("${spring.social.wechat.clientSecret}") String weixinAppSecret;

    @RequestMapping(value = "/getTicket", method = RequestMethod.POST)
    @AppMethod
    public ResponseData getJsTicket(@RequestParam(value = "url", defaultValue = "") String url) {

        Map<String, String> ret = weChatManager.getJsTicket(weixinAppId, weixinAppSecret, url);

        if (ret != null && ret.containsKey("jsapiTicket")) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("noncestr", ret.get("nonceStr"));
            jsonObject.put("jsapiTicket", ret.get("jsapi_ticket"));
            jsonObject.put("timestamp", ret.get("timestamp"));
            jsonObject.put("url", ret.get("url"));
            jsonObject.put("signature", ret.get("signature"));
            jsonObject.put("appId", ret.get("weixinAppId"));

            return new ResponseData(ExceptionMsg.SUCCESS, jsonObject);
        } else {
            return new ResponseData(ExceptionMsg.FAILED);
        }
    }
}
