package com.musixise.musixisebox.domain.result;

/**
 * Created by zhaowei on 2018/4/1.
 */
public enum  ExceptionMsg {
    SUCCESS("0", "success"),
    FAILED("20000","fail"),
    NEED_LOGIN("40000","请登录"),
    ParamError("20001", "参数错误");

    private ExceptionMsg(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    private String code;
    private String msg;

    public String getCode() {
        return code;
    }
    public String getMsg() {
        return msg;
    }
}
