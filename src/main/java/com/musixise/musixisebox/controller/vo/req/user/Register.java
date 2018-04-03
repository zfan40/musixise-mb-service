package com.musixise.musixisebox.controller.vo.req.user;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by zhaowei on 2018/4/3.
 */
public class Register {

    @NotNull
    @Size(min = 5, max = 50)
    private String username;

    @NotNull
    @Size(min = 6, max = 30)
    private String password;


    private String realname;

    private String tel;

    @Email(message="不是有效的 Email 格式")
    private String email;

    private String birth;

    private String gender;

    private String smallAvatar;

    private String largeAvatar;

    private String nation;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSmallAvatar() {
        return smallAvatar;
    }

    public void setSmallAvatar(String smallAvatar) {
        this.smallAvatar = smallAvatar;
    }

    public String getLargeAvatar() {
        return largeAvatar;
    }

    public void setLargeAvatar(String largeAvatar) {
        this.largeAvatar = largeAvatar;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }
}
