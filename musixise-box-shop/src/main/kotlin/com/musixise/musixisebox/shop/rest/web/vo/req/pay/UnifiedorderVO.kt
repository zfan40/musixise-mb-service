package com.musixise.musixisebox.shop.rest.web.vo.req.pay

import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.Size

class UnifiedorderVO {

    @Size(min = 1, max = 50)
    @ApiModelProperty(value = "商品ID", example = "商品ID")
    var productId: Long? = null
}