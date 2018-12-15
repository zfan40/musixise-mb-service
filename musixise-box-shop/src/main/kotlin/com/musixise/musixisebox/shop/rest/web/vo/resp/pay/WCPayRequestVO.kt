package com.musixise.musixisebox.shop.rest.web.vo.resp.pay

import io.swagger.annotations.ApiModelProperty

class WCPayRequestVO {

    @ApiModelProperty(value = "公众号名称", example = "wx2421b1c4370ec43b")
    var appId: String? = null

    @ApiModelProperty(value = "时间戳，自1970年以来的秒数", example = "1395712654")
    var timeStamp: String? = null

    @ApiModelProperty(value = "随机串 ", example = "e61463f8efa94090b1f366cccfbbb444")
    var nonceStr: String? = null

    @ApiModelProperty(value = "预付单字符串", example = "prepay_id=u802345jgfjsdfgsdg888")
    var packageStr: String? = null

    @ApiModelProperty(value = "微信签名方式", example = "MD5")
    var signType: String? = null

    @ApiModelProperty(value = "微信签名 ", example = "70EA570631E4BB79628FBCA90534C63FF7FADD89")
    var paySign: String? = null
}