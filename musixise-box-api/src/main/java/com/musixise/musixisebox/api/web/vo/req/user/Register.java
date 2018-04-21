package com.musixise.musixisebox.api.web.vo.req.user;


import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by zhaowei on 2018/4/3.
 */
public class Register {

    @ApiModelProperty(value = "账号名", required = true, example = "demo", position = 1)
    @NotNull
    @Size(min = 5, max = 50)
    private String username;

    @ApiModelProperty(value = "密码", required = true, example = "demo", position = 2)
    @NotNull
    @Size(min = 6, max = 30)
    private String password;

    @ApiModelProperty(value = "昵称", example = "花花")
    private String realname;

    @ApiModelProperty(value = "电话", example = "13900000000")
    private String tel;

    @ApiModelProperty(value = "邮箱", required = true, example = "demo@musixise.com", position = 2)
    @NotNull
    @Email(message = "不是有效的 Email 格式")
    private String email;

    @ApiModelProperty(value = "生日", example = "2000-01-01")
    private String birth;

    @ApiModelProperty(value = "性别(0=未知，1=男生，2=女生)", example = "0")
    private String gender;

    @ApiModelProperty(value = "头像", example = "https://gw.alicdn.com/tps/TB1fcMYNVXXXXXqXVXXXXXXXXXX-750-750.png")
    private String smallAvatar;

    @ApiModelProperty(value = "头像", example = "https://gw.alicdn.com/tps/TB1fcMYNVXXXXXqXVXXXXXXXXXX-750-750.png")
    private String largeAvatar;

    @ApiModelProperty(value = "国籍", example = "cn")
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

    @Override
    public String toString() {
        return "Register{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", realname='" + realname + '\'' +
                ", tel='" + tel + '\'' +
                ", email='" + email + '\'' +
                ", birth='" + birth + '\'' +
                ", gender='" + gender + '\'' +
                ", smallAvatar='" + smallAvatar + '\'' +
                ", largeAvatar='" + largeAvatar + '\'' +
                ", nation='" + nation + '\'' +
                '}';
    }
}
