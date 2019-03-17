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

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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

    //获取临时素材(视频不能使用https协议)
    public static final String GET_TMP_MATERIAL = "https://api.weixin.qq.com/cgi-bin/media/get?access_token=%s&media_id=%s";
    //获取临时素材(视频)
    public static final String GET_TMP_MATERIAL_VIDEO = "http://api.weixin.qq.com/cgi-bin/media/get?access_token=%s&media_id=%s";


    public File getMedia(String mediaId) {
        return fetchTmpFile(mediaId, "image");
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

    protected File fetchTmpFile(String media_id, String type){
        try {
            String token = getAccessToken(weixinAppId, weixinAppSecret);
            String url = null;
            //视频是http协议
            if("video".equalsIgnoreCase(type)){
                url = String.format(GET_TMP_MATERIAL_VIDEO, token, media_id);
            }else{
                url = String.format(GET_TMP_MATERIAL, token, media_id);;
            }
            URL u = new URL(url);
            HttpURLConnection  conn = (HttpURLConnection) u.openConnection();
            conn.setRequestMethod("POST");
            conn.connect();
            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
            String content_disposition = conn.getHeaderField("content-disposition");

            if (content_disposition == null) {
                //获取 body 内容
                byte[] data1 = new byte[conn.getInputStream().available()];
                conn.getInputStream().read(data1);
                // 转成字符串
                String response = new String(data1);
                logger.info("getMedia {}", response);
                throw new MusixiseException("get media fails");
            }

            //微信服务器生成的文件名称
            String file_name ="";
            String[] content_arr = content_disposition.split(";");
            if(content_arr.length  == 2){
                String tmp = content_arr[1];
                int index = tmp.indexOf("\"");
                file_name =tmp.substring(index+1, tmp.length()-1);
            }
            //生成不同文件名称
            String filePath = System.getProperty("user.dir") + "/tmp/";
            File file = new File(filePath + file_name);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            byte[] buf = new byte[2048];
            int length = bis.read(buf);
            while(length != -1){
                bos.write(buf, 0, length);
                length = bis.read(buf);
            }
            bos.close();
            bis.close();
            return file;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
