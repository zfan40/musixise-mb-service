package com.musixise.musixisebox.api.web.vo.req.follow;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * Created by zhaowei on 2018/4/16.
 */
public class CreateFollowVO {

    @ApiModelProperty(value = "关注的uid", example = "0", required = true)
    @NotNull(message = "关注id不能为空")
    private Long followId;

    @ApiModelProperty(value = "关注状态 (1=关注，0=解除关注)", example = "1")
    private Integer status;

    public Long getFollowId() {
        return followId;
    }

    public void setFollowId(Long followId) {
        this.followId = followId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
