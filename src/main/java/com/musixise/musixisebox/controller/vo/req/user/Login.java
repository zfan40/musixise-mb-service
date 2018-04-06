package com.musixise.musixisebox.controller.vo.req.user;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by zhaowei on 2018/4/3.
 */
public class Login {

    @NotNull
    @Size(min = 1, max = 50)
    @ApiModelProperty(value = "用户名", example = "demo")
    public String userName;

    @NotNull
    @Size(min = 1, max = 50)
    @ApiModelProperty(value = "密码", example = "demo")
    private String passWord;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    @Override
    public String toString() {
        return "Login{" +
                "userName='" + userName + '\'' +
                ", passWord='" + passWord + '\'' +
                '}';
    }
}
