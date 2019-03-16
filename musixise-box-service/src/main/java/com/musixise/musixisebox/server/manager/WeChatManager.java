package com.musixise.musixisebox.server.manager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.musixise.musixisebox.api.exception.MusixiseException;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Verb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by zhaowei on 2018/4/5.
 */
@Component
public class WeChatManager {

    @Value("${spring.social.wechat.clientId}") private String weixinAppId;
    @Value("${spring.social.wechat.clientSecret}") private String weixinAppSecret;

    private static Logger logger = LoggerFactory.getLogger(WeChatManager.class);


    public String getMedia(String mediaId) {

        String accessToken = getAccessToken(weixinAppId, weixinAppSecret);

        if (accessToken != null) {

            try {
                String mediaStream = getMediaStream(accessToken, mediaId);

                if (mediaStream != null) {

                    return mediaStream;

                } else {
                    throw new MusixiseException("get media id fails");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            throw new MusixiseException("get accessToken fails");
        }

        return null;

    }

    private static String getMediaStream(String accessToken, String mediaId)throws IOException {
        String url = "https://api.weixin.qq.com/cgi-bin/media/get";

        String params = "access_token=" + accessToken + "&media_id=" + mediaId;

        InputStream is = null;
        try {
            String urlNameString = url + "?" + params;

            OAuthRequest oAuthRequest = new OAuthRequest(Verb.GET, String.format(urlNameString, accessToken));
            Response response = oAuthRequest.send();
            String responceBody = response.getBody();

            logger.info("getMedia {}", responceBody);

            if (responceBody.indexOf("errcode") != -1) {
                throw new MusixiseException("get media fails");

            } else {
                return responceBody;
            }


        } catch (MusixiseException e) {
            throw new MusixiseException(e.getMessage());
        } catch (Exception e) {
            logger.error("get meidia excpetion", e);
            throw new MusixiseException("get media exception");

        }
    }


    public Map<String, String> getJsTicket(String weixinAppId, String weixinAppSecret, String url) {
        String accessToken = getAccessToken(weixinAppId, weixinAppSecret);

        if (accessToken != null) {
            String jsTicket = getTicket(accessToken);
            if (jsTicket != null) {
                Map<String, String> ret = sign(jsTicket, url);
                return ret;
            } else {
                throw new MusixiseException("get jsTicket fails");
            }
        } else {
            throw new MusixiseException("get accessToken fails");
        }
    }

    private String getTicket(String accessToken) {

        String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%s&type=jsapi";
        OAuthRequest oAuthRequest = new OAuthRequest(Verb.GET, String.format(url, accessToken));
        Response response = oAuthRequest.send();
        String responceBody = response.getBody();
        logger.error("getTicket {}", responceBody);
        Object result = JSON.parse(responceBody);
        if (((JSONObject) result).get("errcode") != null) {
            if (JSONPath.eval(result, "$.errcode").toString().equals("0")) {
                return JSONPath.eval(result, "$.ticket").toString();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private String getAccessToken(String appId, String appSecret) {

        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
        OAuthRequest oAuthRequest = new OAuthRequest(Verb.GET, String.format(url, appId, appSecret));
        Response response = oAuthRequest.send();
        String responceBody = response.getBody();
        logger.error("getAccessToken {}", responceBody);
        Object result = JSON.parse(responceBody);
        if (((JSONObject) result).get("errcode") == null) {
            return JSONPath.eval(result, "$.access_token").toString();
        } else {
            return null;
        }
    }

    private static Map<String, String> sign(String jsapi_ticket, String url) {
        Map<String, String> ret = new HashMap<String, String>();
        String nonce_str = create_nonce_str();
        String timestamp = create_timestamp();
        String string1;
        String signature = "";

        //注意这里参数名必须全部小写，且必须有序
        string1 = "jsapi_ticket=" + jsapi_ticket +
                "&noncestr=" + nonce_str +
                "&timestamp=" + timestamp +
                "&url=" + url;
        System.out.println(string1);

        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        ret.put("url", url);
        ret.put("jsapi_ticket", jsapi_ticket);
        ret.put("nonceStr", nonce_str);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);

        return ret;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    private static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    private static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }


}
