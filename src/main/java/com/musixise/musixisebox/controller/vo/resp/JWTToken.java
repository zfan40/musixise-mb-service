package com.musixise.musixisebox.controller.vo.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by zhaowei on 2018/4/1.
 */
public class JWTToken {

    @ApiModelProperty(value = "登录秘钥", example = "xxxxxxxxxxxxxxxxxx")
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
