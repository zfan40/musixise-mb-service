package com.musixise.musixisebox.controller.vo.req.favorite;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * Created by zhaowei on 2018/4/16.
 */
public class CreateFavoriteVO {

    @ApiModelProperty(value = "作品ID", example = "0", required = true)
    @NotNull(message = "作品id不能为空")
    private Long workId;

    @ApiModelProperty(value = "作品状态(0=公开(default)，1=私人，2=垃圾箱)", example = "1")
    private Integer status;

    public Long getWorkId() {
        return workId;
    }

    public void setWorkId(Long workId) {
        this.workId = workId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
