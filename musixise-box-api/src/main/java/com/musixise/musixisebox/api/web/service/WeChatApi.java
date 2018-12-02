package com.musixise.musixisebox.api.web.service;

import com.alibaba.fastjson.JSONObject;
import com.musixise.musixisebox.api.result.MusixiseResponse;
import com.musixise.musixisebox.api.web.vo.req.wechat.UnifiedorderVO;
import com.musixise.musixisebox.api.web.vo.resp.wechat.WCPayRequestVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zhaowei on 2018/4/21.
 */
@Api(value = "微信 JsTicket", description = "获取微信 JsTicket", tags = "微信相关")
public interface WeChatApi {

    /** 获取 JsTicket
     * @param url
     * @return
     */
    @ApiOperation(value = "获取 JsTicket",notes = "")
    MusixiseResponse<JSONObject> getJsTicket(@RequestParam(value = "url", defaultValue = "") String url);

    /**
     * 下单
     * @param unifiedorderVO
     * @return
     */
    @ApiOperation(value = "下单",notes = "")
    MusixiseResponse<WCPayRequestVO> unifiedorder(Long uid, UnifiedorderVO unifiedorderVO);

    /**
     * 支付通知
     * @param request
     * @return
     */
    @ApiOperation(value = "支付通知",notes = "")
    String payNotify(HttpServletRequest request);
}
