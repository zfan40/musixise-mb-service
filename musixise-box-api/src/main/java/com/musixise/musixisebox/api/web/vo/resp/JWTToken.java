package com.musixise.musixisebox.api.web.vo.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by zhaowei on 2018/4/1.
 */
public class JWTToken {

    @ApiModelProperty(value = "登录秘钥", example = "eyJhbGciOiJIUzI1NiJ9.eyJ1aWQiOjUsInN1YiI6IjUiLCJleHAiOjE1MjU1OTE2MjksImlhdCI6MTUyMjk5OTYyOSwianRpIjoiMTUyMjk5OTYyOTE2NSJ9.f-p9Jwj1Ye3Uba_4ZvOEkgZhCXnecSG5FwmUSIE4l68")
    private String idToken;

    public JWTToken(String idToken) {
        this.idToken = idToken;
    }

    @JsonProperty("id_token")
    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }
}
