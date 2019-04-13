package com.musixise.musixisebox.api.web.vo.req.home;

import io.swagger.annotations.ApiModelProperty;

public class QueryWork {

    /**
     * 作品分类
     */
    @ApiModelProperty(value = "作品分类", example = "1=母情节")
    private Integer category;

    @ApiModelProperty(value = "作品排序策略", example = "1=按PV, 2=按收藏量")
    private Integer orderStrategy;

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public Integer getOrderStrategy() {
        return orderStrategy;
    }

    public void setOrderStrategy(Integer orderStrategy) {
        this.orderStrategy = orderStrategy;
    }
}
