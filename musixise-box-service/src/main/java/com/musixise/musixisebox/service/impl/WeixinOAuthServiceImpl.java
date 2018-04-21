package com.musixise.musixisebox.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.musixise.musixisebox.api.exception.MusixiseException;
import com.musixise.musixisebox.api.web.vo.resp.SocialVO;
import com.musixise.musixisebox.service.CustomOAuthService;
import com.musixise.musixisebox.config.OAuthTypesConstants;
import org.scribe.builder.api.DefaultApi20;
import org.scribe.model.*;
import org.scribe.oauth.OAuth20ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhaowei on 2018/4/5.
 */
public class WeixinOAuthServiceImpl extends OAuth20ServiceImpl implements CustomOAuthService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final DefaultApi20 api;
    private final OAuthConfig config;
    private final String authorizationUrl;

    public WeixinOAuthServiceImpl(DefaultApi20 api, OAuthConfig config, DefaultApi20 api1, OAuthConfig config1, String authorizationUrl) {
        super(api, config);
        this.api = api1;
        this.config = config1;
        this.authorizationUrl = authorizationUrl;
    }

    public WeixinOAuthServiceImpl(DefaultApi20 api, OAuthConfig config) {
        super(api, config);
        this.api = api;
        this.config = config;
        this.authorizationUrl = getAuthorizationUrl(null);
    }

    @Override
    public Token getAccessToken(Token requestToken, Verifier verifier) {
        OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());
        request.addQuerystringParameter("appid", config.getApiKey());
        request.addQuerystringParameter("secret", config.getApiSecret());
        request.addQuerystringParameter(OAuthConstants.CODE, verifier.getValue());
        if(config.hasScope()) {
            request.addQuerystringParameter(OAuthConstants.SCOPE, config.getScope());
        }
        Response response = request.send();
        String responceBody = response.getBody();
        log.trace("get accesstoken fail {}", responceBody);
        Object result = JSON.parse(responceBody);
        if (((JSONObject) result).get("errcode") != null) {
            log.error("get accesstoken fail {}", responceBody);
            throw new MusixiseException("获取微信 AccessToken 失败");
        } else {
            return new Token(JSONPath.eval(result, "$.access_token").toString(), "", responceBody);
        }
    }

    @Override
    public SocialVO getOAuthUser(Token accessToken) {
        //返回openID
        Object result = JSON.parse(accessToken.getRawResponse());
        String openId =  JSONPath.eval(result, "$.openid").toString();
        SocialVO socialInfoDTO = getUserInfo(accessToken.getToken(), openId);

        if (socialInfoDTO != null) {

            socialInfoDTO.setAccessToken(JSONPath.eval(result, "$.access_token").toString());
            socialInfoDTO.setRefreshToken(JSONPath.eval(result, "$.refresh_token").toString());
            int expiresIn = (int)System.currentTimeMillis()/1000 + Integer.valueOf(JSONPath.eval(result, "$.expires_in").toString());
            socialInfoDTO.setExpiresIn(expiresIn);
            return socialInfoDTO;
        }

        throw new MusixiseException("获取微信用户授权失败");
    }

    // https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN
    private SocialVO getUserInfo(String accessToken, String openId) {
        String USER_INFO_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN";
        OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), String.format(USER_INFO_URL, accessToken, openId));
        Response response = request.send();
        String responceBody = response.getBody();
        log.trace("get getuserinfo fail {}", responceBody);
        Object result = JSON.parse(responceBody);
        if (((JSONObject) result).get("errcode") != null) {
            log.error("get getuserinfo fail {}", responceBody);
            throw new MusixiseException("获取微信用户信息失败");
        } else {
            SocialVO socialVO = new SocialVO();
            socialVO.setOpenId(JSONPath.eval(result, "$.openid").toString());
            socialVO.setProvider(getoAuthType());
            socialVO.setAvatar(JSONPath.eval(result, "$.headimgurl").toString());
            socialVO.setNickName(JSONPath.eval(result, "$.nickname").toString());
            socialVO.setSex(Integer.valueOf(JSONPath.eval(result, "$.sex").toString()));
            return socialVO;
        }
    }

    @Override
    public String getoAuthType() {
        return OAuthTypesConstants.WECHAT;
    }

    @Override
    public String getAuthorizationUrl() {
        return authorizationUrl;
    }
}
