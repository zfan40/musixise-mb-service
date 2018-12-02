package com.musixise.musixisebox.api.web.vo.req.wechat;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UnifiedorderVO {

    @NotNull
    @Size(min = 1, max = 50)
    @ApiModelProperty(value = "商品ID", example = "商品ID")
    private Long productId;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
