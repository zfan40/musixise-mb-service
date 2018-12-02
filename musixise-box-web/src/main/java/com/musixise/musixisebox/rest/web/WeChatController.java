package com.musixise.musixisebox.rest.web;

import com.alibaba.fastjson.JSONObject;
import com.musixise.musixisebox.api.enums.ExceptionMsg;
import com.musixise.musixisebox.api.result.MusixiseResponse;
import com.musixise.musixisebox.api.web.service.WeChatApi;
import com.musixise.musixisebox.api.web.vo.req.wechat.UnifiedorderVO;
import com.musixise.musixisebox.api.web.vo.resp.wechat.WCPayRequestVO;
import com.musixise.musixisebox.server.aop.AppMethod;
import com.musixise.musixisebox.server.manager.WeChatManager;
import com.musixise.musixisebox.server.service.PayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by zhaowei on 2018/4/5.
 */
@RestController
@RequestMapping("/api/v1/wechat")
public class WeChatController implements WeChatApi {

    private @Resource WeChatManager weChatManager;

    private @Resource PayService PayServiceImpl;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

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

    @Override
    @AppMethod(isLogin = false)
    @RequestMapping(value = "/unifiedorder", method = RequestMethod.POST)
    public MusixiseResponse<WCPayRequestVO> unifiedorder(Long uid, UnifiedorderVO unifiedorderVO) {

        try {

            WCPayRequestVO payInfo = PayServiceImpl.getPayInfo(unifiedorderVO.getProductId());
            return new MusixiseResponse<>(ExceptionMsg.SUCCESS, payInfo);
        } catch (Exception e) {
            logger.error("Exception do unifiedorder action: ", e);
            return new MusixiseResponse<>(ExceptionMsg.FAILED);
        }

    }

    @Override
    @RequestMapping(value = "/notify", method = {RequestMethod.POST, RequestMethod.GET})
    public String payNotify(HttpServletRequest request) {
        return PayServiceImpl.getPayNotify(request);
    }
}
