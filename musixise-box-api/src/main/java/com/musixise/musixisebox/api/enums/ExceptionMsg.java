package com.musixise.musixisebox.api.enums;

/**
 * Created by zhaowei on 2018/4/1.
 */
public enum  ExceptionMsg {
    SUCCESS("0", "success"),
    FAILED("20000","fail"),
    PARAM_ERROR("20001", "params error"),
    NOT_EXIST("30000", "record not found"),
    NEED_LOGIN("40000","请登录"),
    EMAIL_USED("40001","邮箱已被使用"),
    USERNAME_USED("40002","用户名已被使用"),
    UPLOAD_ERROR("50000","upload error");



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
