package com.musixise.musixisebox.controller.vo.req.user;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by zhaowei on 2018/4/3.
 */
public class Login {

    @NotNull
    @Size(min = 1, max = 50)
    public String userName;

    @NotNull
    @Size(min = 1, max = 50)
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
}
