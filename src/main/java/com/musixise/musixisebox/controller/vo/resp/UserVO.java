package com.musixise.musixisebox.controller.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by zhaowei on 2018/4/3.
 */
@ApiModel(value="UserVO", description="用户模型")
public class UserVO {

    @ApiModelProperty(value = "主键", example = "1")
    private Long id;

    @ApiModelProperty(value = "账号名", example = "demo")
    private String username;

    @ApiModelProperty(value = "用户昵称", example = "mumu")
    private String realname;

    @ApiModelProperty(value = "电话", example = "13900000000")
    private String tel;

    @ApiModelProperty(value = "Email", example = "demo@musixise.com")
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

    @ApiModelProperty(value = "是否音乐人(0=普通，1=音乐人)", example = "0")
    private Integer isMaster;

    @ApiModelProperty(value = "个人说明", example = "this is intro")
    private String brief;

    @ApiModelProperty(value = "关注状态(0=未关注，1=已关注)", example = "0")
    private Integer followStatus;

    @ApiModelProperty(value = "关注数量", example = "99")
    private Integer followNum;

    @ApiModelProperty(value = "粉丝数量", example = "99")
    private Integer fansNum;

    @ApiModelProperty(value = "作品数量", example = "99")
    private Integer songNum;

    @ApiModelProperty(value = "用户ID", example = "88888")
    private Long userId;

    @ApiModelProperty(value = "加入日期", example = "2018-04-06 14:00:04")
    private String createdDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public Integer getIsMaster() {
        return isMaster;
    }

    public void setIsMaster(Integer isMaster) {
        this.isMaster = isMaster;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public Integer getFollowStatus() {
        return followStatus;
    }

    public void setFollowStatus(Integer followStatus) {
        this.followStatus = followStatus;
    }

    public Integer getFollowNum() {
        return followNum;
    }

    public void setFollowNum(Integer followNum) {
        this.followNum = followNum;
    }

    public Integer getFansNum() {
        return fansNum;
    }

    public void setFansNum(Integer fansNum) {
        this.fansNum = fansNum;
    }

    public Integer getSongNum() {
        return songNum;
    }

    public void setSongNum(Integer songNum) {
        this.songNum = songNum;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
