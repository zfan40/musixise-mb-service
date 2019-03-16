package com.musixise.musixisebox.api.web.service;

import com.alibaba.fastjson.JSONObject;
import com.musixise.musixisebox.api.result.MusixiseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by zhaowei on 2018/4/21.
 */
@Api(value = "微信接口", description = "获取微信 JsTicket", tags = "微信相关")
public interface WeChatApi {

    /** 获取 JsTicket
     * @param url
     * @return
     */
    @ApiOperation(value= "获取 JsTicket",notes = "")
    MusixiseResponse<JSONObject> getJsTicket(@RequestParam(value = "url", defaultValue = "") String url);

    /**
     * 保存临时素材接口
     * @param mediaId
     * @return
     */
    @ApiOperation(value= "保存临时素材接口",notes = "")
    MusixiseResponse<String> saveMedia(@RequestParam(value = "media_id", defaultValue = "") String mediaId);

}
