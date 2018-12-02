package com.musixise.musixisebox.server.service.impl;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import com.musixise.musixisebox.api.web.vo.resp.wechat.WCPayRequestVO;
import com.musixise.musixisebox.server.config.pay.MyWxConfig;
import com.musixise.musixisebox.server.service.PayService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component("payServiceImpl")
public class PayServiceImpl implements PayService {

    @Resource MyWxConfig config;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public WCPayRequestVO getPayInfo(Long productId) throws Exception {

        //参数需要重新进行签名计算，参与签名的参数为：appId、timeStamp、nonceStr、package、signType，参数区分大小写。

        WXPay wxpay = new WXPay(config);
        String prepayId = getPrepayId(productId);

        Map<String, String> reqData = new HashMap<>();
        reqData.put("appId", config.getAppID());
        reqData.put("nonceStr", WXPayUtil.generateNonceStr());
        reqData.put("signType", "MD5");
        reqData.put("timeStamp", String.valueOf(System.currentTimeMillis()));
        reqData.put("package", "prepay_id="+prepayId);

        //计算签名
        String signature = WXPayUtil.generateSignature(reqData, config.getKey());

        WCPayRequestVO wcPayRequestVO = new WCPayRequestVO();

        wcPayRequestVO.setAppId(config.getAppID());
        wcPayRequestVO.setTimeStamp(reqData.get("timeStamp"));
        wcPayRequestVO.setNonceStr(reqData.get("nonceStr"));
        wcPayRequestVO.setPackageStr(reqData.get("package"));
        wcPayRequestVO.setSignType("MD5");
        wcPayRequestVO.setPaySign(signature);

        return wcPayRequestVO;
    }

    @Override
    public String getPayNotify(HttpServletRequest request) {

        String result = null;
        String inlength;
        String notifyXml = "";
        try {
            while ((inlength = request.getReader().readLine()) != null) {
                notifyXml += inlength;
            }
        } catch (IOException e) {
            //获取XML错误
            logger.error("Exception parse wechat notify data: "+ request.toString());
        }

        if (StringUtils.isEmpty(notifyXml)) {
            //xml为空
            logger.warn("xml of wechat pay is empty");
            return null;
        }

        try {

            Map<String, String> map = WXPayUtil.xmlToMap(notifyXml);
            String appid = map.get("appid");
            String bank_type = map.get("bank_type");
            String cash_fee = map.get("cash_fee");
            String device_info = map.get("device_info");
            String fee_type = map.get("fee_type");
            String is_subscribe = map.get("is_subscribe");
            String mch_id = map.get("mch_id");
            String nonce_str = map.get("nonce_str");
            String openid = map.get("openid");
            String out_trade_no = map.get("out_trade_no");
            String result_code = map.get("result_code");
            String return_code = map.get("return_code");
            String sign = map.get("sign");
            String time_end = map.get("time_end");
            String total_fee = map.get("total_fee");
            String trade_type = map.get("trade_type");
            String transaction_id = map.get("transaction_id");

            Map<String, String> date = new HashMap<>();
            date.put("appid", appid);
            date.put("bank_type", bank_type);
            date.put("cash_fee", cash_fee);
            date.put("device_info", device_info);
            date.put("fee_type", fee_type);
            date.put("is_subscribe", is_subscribe);
            date.put("mch_id", mch_id);
            date.put("nonce_str", nonce_str);
            date.put("openid", openid);
            date.put("out_trade_no", out_trade_no);
            date.put("result_code", result_code);
            date.put("return_code", return_code);
            date.put("time_end", time_end);
            date.put("total_fee", total_fee);
            date.put("trade_type", trade_type);
            date.put("transaction_id", transaction_id);

            String localSign = null;
            localSign = WXPayUtil.generateSignature(date, config.getKey());

            if (localSign.equals(sign)) {
                if (result_code.equals("SUCCESS") && return_code.equals("SUCCESS")) {
                    //业务模块处理点
                    logger.info("wechat notify done. "+ date.toString());

                } else {
                    //签名失败
                    logger.error("wechat notify sign error: "+ date.toString());
                    return null;
                }
            }

            }catch(Exception e){
                //转化XML错误
                logger.error("wechat notify format error. ", e.toString());
                e.printStackTrace();
                return null;
            }
            //正确的结果要分上面几个不一样返回，不能这样只返回一个
            result = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                    + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";

            return result;
    }

    @Override
    public String getPrepayId(Long productId) throws Exception {

        WXPay wxpay = new WXPay(config);

        Map<String, String> data = new HashMap<>();
        data.put("body", "腾讯充值中心-QQ会员充值");
        data.put("out_trade_no", "2016090910595900000012"+productId);
        data.put("device_info", "");
        data.put("fee_type", "CNY");
        data.put("total_fee", "1");
        data.put("spbill_create_ip", "123.12.12.123");
        data.put("notify_url", "http://api.octave-love.com/api/v1/wechat/notify");
        data.put("trade_type", "NATIVE");  // 此处指定为扫码支付
        data.put("product_id", productId.toString());

        try {
            Map<String, String> resp = wxpay.unifiedOrder(data);
            if (resp.get("return_code").equals("FAIL")) {
                logger.error("Exception do unifiedorder action: "+ resp.toString());
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "prePayId";
    }
}
