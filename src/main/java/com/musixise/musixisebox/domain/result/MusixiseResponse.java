package com.musixise.musixisebox.domain.result;

import io.swagger.annotations.ApiModel;

/**
 * Created by zhaowei on 2018/4/1.
 */

@ApiModel(parent = Response.class)
public class MusixiseResponse<T> extends Response {

    protected T data;

    public MusixiseResponse(T data) {
        this.data = data;
    }

    public static MusixiseResponse successResponse(Object msg) {
        return new MusixiseResponse(ExceptionMsg.SUCCESS, msg);
    }

    public static MusixiseResponse errorResponse(String msg) {
        return new MusixiseResponse(ExceptionMsg.FAILED, msg);
    }

    public MusixiseResponse(ExceptionMsg msg) {
        super(msg);
    }

    public MusixiseResponse(String rspCode, String rspMsg) {
        super(rspCode, rspMsg);
    }

    public MusixiseResponse(String rspCode, String rspMsg, T  data) {
        super(rspCode, rspMsg);
        this.data = data;
    }

    public MusixiseResponse(ExceptionMsg msg, T  data) {
        super(msg);
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T  data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "MusixiseResponse{" +
                "data=" + data +
                "} " + super.toString();
    }

}
