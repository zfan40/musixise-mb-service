package com.musixise.musixisebox.api.web.vo.resp.wechat;

import io.swagger.annotations.ApiModelProperty;

public class WCPayRequestVO {

    @ApiModelProperty(value = "公众号名称", example = "wx2421b1c4370ec43b")
    private String appId;

    @ApiModelProperty(value = "时间戳，自1970年以来的秒数", example = "1395712654")
    private String timeStamp;

    @ApiModelProperty(value = "随机串 ", example = "e61463f8efa94090b1f366cccfbbb444")
    private String nonceStr;

    @ApiModelProperty(value = "预付单字符串", example = "prepay_id=u802345jgfjsdfgsdg888")
    private String packageStr;

    @ApiModelProperty(value = "微信签名方式", example = "MD5")
    private String signType;

    @ApiModelProperty(value = "微信签名 ", example = "70EA570631E4BB79628FBCA90534C63FF7FADD89")
    private String paySign;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getPackageStr() {
        return packageStr;
    }

    public void setPackageStr(String packageStr) {
        this.packageStr = packageStr;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getPaySign() {
        return paySign;
    }

    public void setPaySign(String paySign) {
        this.paySign = paySign;
    }
}
