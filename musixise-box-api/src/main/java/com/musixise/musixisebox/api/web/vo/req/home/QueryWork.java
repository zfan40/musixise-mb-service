package com.musixise.musixisebox.api.web.vo.req.home;

import io.swagger.annotations.ApiModelProperty;

public class QueryWork {

    /**
     * 作品分类
     */
    @ApiModelProperty(value = "作品分类", example = "1=母情节")
    private Integer category;

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }
}
