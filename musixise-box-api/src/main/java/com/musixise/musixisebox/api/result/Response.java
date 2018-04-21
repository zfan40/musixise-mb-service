package com.musixise.musixisebox.api.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.musixise.musixisebox.api.enums.ExceptionMsg;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by zhaowei on 2018/4/1.
 */
public class Response {

    @JsonProperty("errcode")
    @ApiModelProperty(value = "状态码", example = "0", allowableValues="0=成功, 20000=失败, 20001=参数错误", position = 1)
    private String rspCode="0";

    @ApiModelProperty(value = "原因", example = "success", reference= "", position = 2)
    @JsonProperty("resmsg")
    private String rspMsg="success";

    public Response() {
    }

    public Response(ExceptionMsg msg){
        this.rspCode=msg.getCode();
        this.rspMsg=msg.getMsg();
    }

    public Response(String rspCode) {
        this.rspCode = rspCode;
        this.rspMsg = "";
    }

    public Response(String rspCode, String rspMsg) {
        this.rspCode = rspCode;
        this.rspMsg = rspMsg;
    }
    public String getRspCode() {
        return rspCode;
    }
    public void setRspCode(String rspCode) {
        this.rspCode = rspCode;
    }
    public String getRspMsg() {
        return rspMsg;
    }
    public void setRspMsg(String rspMsg) {
        this.rspMsg = rspMsg;
    }

    @Override
    public String toString() {
        return "Response{" +
                "rspCode='" + rspCode + '\'' +
                ", rspMsg='" + rspMsg + '\'' +
                '}';
    }

}
