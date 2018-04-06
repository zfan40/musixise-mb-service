package com.musixise.musixisebox.controller.vo.req.user;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by zhaowei on 2018/4/3.
 */
public class Update {

    @ApiModelProperty(value = "用户昵称", example = "mumu")
    private String realname;

    @ApiModelProperty(value = "电话", example = "13900000000")
    private String tel;

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

    @ApiModelProperty(value = "个人说明", example = "this is intro")
    private String brief;

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

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }
}
