package com.musixise.musixisebox.controller.vo.resp.follow;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by zhaowei on 2018/4/4.
 */
public class FollowVO {

    private Long id;

    @ApiModelProperty(value = "用户ID", example = "88888")
    private Long userId;

    @ApiModelProperty(value = "头像", example = "https://gw.alicdn.com/tps/TB1fcMYNVXXXXXqXVXXXXXXXXXX-750-750.png")
    private String smallAvatar;

    @ApiModelProperty(value = "头像", example = "https://gw.alicdn.com/tps/TB1fcMYNVXXXXXqXVXXXXXXXXXX-750-750.png")
    private String largeAvatar;

    @ApiModelProperty(value = "操作时间", example = "2018-04-06 17:00:17")
    private String createdDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
